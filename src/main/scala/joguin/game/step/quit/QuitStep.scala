package joguin.game.step.quit

import cats.free.Free
import eu.timepit.refined._
import joguin.game.progress.GameProgress
import joguin.game.step.{GameOver, GameStep, SaveGame}
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.{MessageSourceOps, MessagesOps, QuitMessageSource}

final class QuitStep(
  implicit i: InteractionOps[QuitF],
  m: MessagesOps[QuitF],
  s: MessageSourceOps[QuitF],
) {
  import i._
  import m._
  import s._
  import QuitMessageSource._

  def start(gameProgress: GameProgress): Free[QuitF, GameStep] = {
    val messageSource = getLocalizedMessageSource(QuitMessageSource)

    val answer = for {
      src <- messageSource
      wantToSaveGame <- getMessage(src)(want_to_save_game)
      invalidOption <- getMessage(src)(error_invalid_option)
      answer <- ask(wantToSaveGame, invalidOption, QuitOption.parse)
    } yield answer

    answer.map {
      case Yes => SaveGame(gameProgress)
      case No => GameOver
    }
  }
}

trait QuitOption
case object Yes extends QuitOption
case object No extends QuitOption

object QuitOption {
  def parse(s: String): Option[QuitOption] =
    refineV[QuitOptionR](s.toLowerCase).toOption.map(_.value match {
      case "y" => Yes
      case "n" => No
    })
}

package joguin.game.step.quit

import cats.free.Free
import eu.timepit.refined._
import joguin.game.progress.GameProgress
import joguin.game.step.GameStepOps.NextGameStep
import joguin.game.step.{GameOver, SaveGame}
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.{MessageSourceOps, MessagesOps, QuitMessageSource}

trait QuitAnswer
case object Yes extends QuitAnswer
case object No extends QuitAnswer

final class QuitStep(
  implicit I: InteractionOps[QuitF],
  M: MessagesOps[QuitF],
  S: MessageSourceOps[QuitF],
) {
  import InteractionOps._
  import M._
  import S._

  def start(gameProgress: GameProgress): Free[QuitF, NextGameStep] = {
    val messageSource = getLocalizedMessageSource(QuitMessageSource)

    val answer = for {
      src <- messageSource
      wantToSaveGame <- getMessage(src, "want-to-save-game")
      invalidOption <- getMessage(src, "error-invalid-option")

      answer <- ask(
        wantToSaveGame,
        invalidOption,
        parseAnswer
      )
    } yield answer

    answer.map {
      case Yes => SaveGame(gameProgress)
      case No  => GameOver
    }
  }

  private def parseAnswer(answer: String): Option[QuitAnswer] =
    refineV[QuitAnswers](answer.toLowerCase).toOption.map(_.value match {
      case "y" => Yes
      case "n" => No
    })
}

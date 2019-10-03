package joguin.game.step.quit

import cats.free.Free
import joguin.game.progress.GameProgress
import joguin.game.step.GameOver
import joguin.game.step.GameStep
import joguin.game.step.SaveGame
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.MessageSourceOps
import joguin.playerinteraction.message.MessagesOps
import joguin.playerinteraction.message.QuitMessageSource

final class QuitStep[F[_]](
  implicit
  i: InteractionOps[F],
  m: MessagesOps[F],
  s: MessageSourceOps[F]
) {
  import QuitMessageSource._
  import i._
  import m._
  import s._

  def play(gameProgress: GameProgress): Free[F, GameStep] = {
    val messageSource = getLocalizedMessageSource(QuitMessageSource)

    val answer = for {
      src            <- messageSource
      wantToSaveGame <- getMessage(src)(want_to_save_game)
      invalidOption  <- getMessage(src)(error_invalid_option)
      answer         <- ask(wantToSaveGame, invalidOption, QuitOption.parse)
    } yield answer

    answer.map {
      case Yes => SaveGame(gameProgress)
      case No  => GameOver
    }
  }
}

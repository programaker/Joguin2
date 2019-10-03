package joguin.game.step.savegame
import cats.free.Free
import joguin.game.progress.GameProgress
import joguin.game.progress.GameProgressRepositoryOps
import joguin.game.step.GameOver
import joguin.game.step.GameStep
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.MessageSourceOps
import joguin.playerinteraction.message.MessagesOps
import joguin.playerinteraction.message.SaveGameMessageSource

final class SaveGameStep[F[_]](
  implicit
  i: InteractionOps[F],
  m: MessagesOps[F],
  s: MessageSourceOps[F],
  r: GameProgressRepositoryOps[F]
) {
  import SaveGameMessageSource._
  import i._
  import m._
  import r._
  import s._

  def play(gameProgress: GameProgress): Free[F, GameStep] =
    for {
      success <- save(gameProgress)
      src     <- getLocalizedMessageSource(SaveGameMessageSource)

      message <- if (success) {
        getMessage(src)(save_game_success)
      } else {
        getMessage(src)(save_game_error)
      }

      _ <- writeMessage(message)
    } yield GameOver
}

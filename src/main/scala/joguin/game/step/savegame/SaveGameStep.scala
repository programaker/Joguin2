package joguin.game.step.savegame
import cats.free.Free
import joguin.game.progress.{GameProgress, GameProgressRepositoryOps}
import joguin.game.step.{GameOver, GameStep}
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.{MessageSourceOps, MessagesOps, SaveGameMessageSource}

final class SaveGameStep(
  implicit i: InteractionOps[SaveGameF],
  m: MessagesOps[SaveGameF],
  s: MessageSourceOps[SaveGameF],
  r: GameProgressRepositoryOps[SaveGameF]
) {
  import SaveGameMessageSource._
  import i._
  import m._
  import r._
  import s._

  def start(gameProgress: GameProgress): Free[SaveGameF, GameStep] =
    for {
      success <- save(gameProgress)
      src <- getLocalizedMessageSource(SaveGameMessageSource)

      message <- if (success) {
        getMessage(src)(save_game_success)
      } else {
        getMessage(src)(save_game_error)
      }

      _ <- writeMessage(message)
    } yield GameOver
}

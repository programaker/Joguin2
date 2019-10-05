package joguin.game.step

import cats.free.Free
import joguin.game.progress.GameProgress
import joguin.game.progress.GameProgressRepositoryOps
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.MessagesOps
import joguin.playerinteraction.message.SaveGameMessageSource

package object savegame {
  def playSaveGameStep[F[_]](gameProgress: GameProgress)(
    implicit
    i: InteractionOps[F],
    m: MessagesOps[F],
    r: GameProgressRepositoryOps[F]
  ): Free[F, GameStep] = {
    import SaveGameMessageSource._
    import i._
    import m._
    import r._

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
}

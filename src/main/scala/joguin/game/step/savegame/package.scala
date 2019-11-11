package joguin.game.step

import cats.free.Free
import joguin.game.progress.GameProgress
import joguin.game.step.GameStep.GameOver
import joguin.playerinteraction.message.MessageSource.SaveGameMessageSource
import joguin.playerinteraction.message.MessageSource.SaveGameMessageSource._

package object savegame {
  def playSaveGameStep[F[_]](gameProgress: GameProgress)(implicit env: SaveGameStepEnv[F]): Free[F, GameStep] = {
    import env._
    import gameProgressRepositoryOps._
    import interactionOps._
    import messageOps._

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

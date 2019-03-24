package joguin.game.step.savegame
import cats.free.Free
import joguin.game.progress.{GameProgress, GameProgressRepositoryOps}
import joguin.game.step.GameOver
import joguin.game.step.GameStepOps.NextGameStep
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.{MessageSourceOps, MessagesOps, SaveGameMessageSource}

class SaveGameStep(
  implicit I: InteractionOps[SaveGameF],
  M: MessagesOps[SaveGameF],
  S: MessageSourceOps[SaveGameF],
  R: GameProgressRepositoryOps[SaveGameF]
) {
  import I._
  import M._
  import R._
  import S._

  def start(gameProgress: GameProgress): Free[SaveGameF, NextGameStep] =
    for {
      success <- save(gameProgress)

      src <- getLocalizedMessageSource(SaveGameMessageSource)
      message <- if (success) {
        getMessage(src, "success")
      } else {
        getMessage(src, "error")
      }

      _ <- writeMessage(message)
    } yield GameOver
}

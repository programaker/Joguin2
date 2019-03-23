package joguin.game.step

import cats.data.EitherK
import cats.free.Free
import cats.free.Free._
import cats.implicits._
import joguin.game.progress.GameProgress
import joguin.game.step.GameStep.NextGameStep
import joguin.game.step.QuitStep._
import joguin.playerinteraction.interaction.{Interaction, InteractionOp}
import joguin.playerinteraction.message.{LocalizedMessageSource, Messages, MessagesOp, QuitMessageSource}

final class QuitStep(
  implicit I: Interaction[QuitOp],
  M: Messages[QuitOp]
) {
  import M._, Interaction._

  def start(gameProgress: GameProgress): Free[QuitOp,NextGameStep] = {
    val option: Free[QuitOp,String] = for {
      wantToSaveGame <- message(msrc, "want-to-save-game")
      invalidOption <- message(msrc, "error-invalid-option")

      option <- ask(
        wantToSaveGame,
        invalidOption,
        _.toLowerCase.some,
        validateSaveGame
      )
    } yield option

    option.flatMap {
      case "y" => pure[QuitOp,NextGameStep](SaveGame(gameProgress))
      case _ => pure[QuitOp,NextGameStep](GameOver)
    }
  }

  private def validateSaveGame(saveGame: String): Boolean =
    saveGame === "y" || saveGame === "n"
}

object QuitStep {
  type QuitOp[A] = EitherK[InteractionOp,MessagesOp,A]
  val msrc: LocalizedMessageSource = LocalizedMessageSource.of(QuitMessageSource)
}

package joguin.game.step.quit

import cats.free.Free
import cats.free.Free._
import eu.timepit.refined._
import joguin.game.progress.GameProgress
import joguin.game.step.GameStep.NextGameStep
import joguin.game.step.{GameOver, SaveGame}
import joguin.playerinteraction.interaction.Interaction
import joguin.playerinteraction.message.{LocalizedMessageSource, Messages, QuitMessageSource}

trait QuitAnswer
case object Yes extends QuitAnswer
case object No extends QuitAnswer

final class QuitStep(
  implicit I: Interaction[QuitOp],
  M: Messages[QuitOp]
) {
  import Interaction._
  import M._

  def start(gameProgress: GameProgress): Free[QuitOp,NextGameStep] = {
    val answer = for {
      wantToSaveGame <- message(src, "want-to-save-game")
      invalidOption <- message(src, "error-invalid-option")

      answer <- ask(
        wantToSaveGame,
        invalidOption,
        parseAnswer
      )
    } yield answer

    answer.flatMap {
      case Yes => pure[QuitOp,NextGameStep](SaveGame(gameProgress))
      case No => pure[QuitOp,NextGameStep](GameOver)
    }
  }

  private def parseAnswer(answer: String): Option[QuitAnswer] =
    refineV[QuitAnswers](answer.toLowerCase).toOption.map(_.value match {
      case "y" => Yes
      case "n" => No
    })

  private val src: LocalizedMessageSource = LocalizedMessageSource.of(QuitMessageSource)
}

package joguin.game.step.showintro

import cats.free.Free
import cats.free.Free._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.game.progress.{GameProgress, GameProgressRepository}
import joguin.game.step.GameStep.NextGameStep
import joguin.game.step.{CreateCharacter, Explore, GameOver}
import joguin.playerinteraction.interaction.Interaction
import joguin.playerinteraction.message.{LocalizedMessageSource, Messages, ShowIntroMessageSource}

sealed trait ShowIntroAnswer
case object NewGame extends ShowIntroAnswer
case object RestoreGame extends ShowIntroAnswer
case object QuitGame extends ShowIntroAnswer

final class ShowIntroStep(
  implicit I: Interaction[ShowIntroOp],
  M: Messages[ShowIntroOp],
  R: GameProgressRepository[ShowIntroOp]
) {
  import I._
  import Interaction._
  import M._
  import R._

  def start: Free[ShowIntroOp,NextGameStep] = {
    val answer = for {
      intro <- message(src, "intro")
      _ <- writeMessage(intro)
      hasSavedProgress <- savedProgressExists

      startKey = if (hasSavedProgress) "start-with-resume" else "start"
      startMessage <- message(src, startKey)
      errorMessage <- message(src, "error-invalid-option")

      answer <- ask(
        startMessage,
        errorMessage,
        parseAnswer(_, hasSavedProgress)
      )
    } yield answer

    answer.flatMap {
      case NewGame =>
        pure[ShowIntroOp,NextGameStep](CreateCharacter)

      case RestoreGame =>
        restore.flatMap {
          _.map(gameProgress => welcomeBack(gameProgress))
            .getOrElse(pure[ShowIntroOp,NextGameStep](CreateCharacter))
        }

      case QuitGame =>
        pure[ShowIntroOp,NextGameStep](GameOver)
    }
  }

  private def parseAnswer(answer: String, hasSavedProgress: Boolean): Option[ShowIntroAnswer] = {
    val sanitizedAnswer = answer.toLowerCase()

    val refAnswer =
      if (hasSavedProgress) {
        refineV[ShowIntroAnswers](sanitizedAnswer)
      } else {
        refineV[ShowIntroAnswersNoRestore](sanitizedAnswer)
      }

    refAnswer.toOption.map(_.value match {
      case "n" => NewGame
      case "q" => QuitGame
      case "r" => RestoreGame
    })
  }

  private def welcomeBack(gp: GameProgress): Free[ShowIntroOp,NextGameStep] = {
    val name: String = gp.mainCharacter.name
    val experience: Int = gp.mainCharacterExperience

    message(src, "welcome-back", name, experience.toString).flatMap { _ =>
      pure[ShowIntroOp,NextGameStep](Explore(gp))
    }
  }

  private val src: LocalizedMessageSource = LocalizedMessageSource.of(ShowIntroMessageSource)
}

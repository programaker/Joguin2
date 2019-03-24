package joguin.game.step.showintro

import cats.free.Free
import cats.free.Free._
import cats.implicits._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.game.progress.{GameProgress, GameProgressRepositoryOps}
import joguin.game.step.GameStepOps.NextGameStep
import joguin.game.step.{CreateCharacter, Explore, GameOver}
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.{LocalizedMessageSource, MessageSourceOps, MessagesOps, ShowIntroMessageSource}

sealed trait ShowIntroAnswer
case object NewGame extends ShowIntroAnswer
case object RestoreGame extends ShowIntroAnswer
case object QuitGame extends ShowIntroAnswer

final class ShowIntroStep(
  implicit I: InteractionOps[ShowIntroF],
  M: MessagesOps[ShowIntroF],
  S: MessageSourceOps[ShowIntroF],
  R: GameProgressRepositoryOps[ShowIntroF]
) {
  import I._
  import InteractionOps._
  import M._
  import R._
  import S._

  def start: Free[ShowIntroF, NextGameStep] = {
    val messageSource = getLocalizedMessageSource(ShowIntroMessageSource)

    val answer = for {
      src <- messageSource
      intro <- getMessage(src, "intro")
      _ <- writeMessage(intro)
      hasSavedProgress <- savedProgressExists

      startKey = if (hasSavedProgress) "start-with-resume" else "start"
      startMessage <- getMessage(src, startKey)
      errorMessage <- getMessage(src, "error-invalid-option")

      answer <- ask(
        startMessage,
        errorMessage,
        parseAnswer(_, hasSavedProgress)
      )
    } yield answer

    answer.flatMap {
      case NewGame =>
        pure[ShowIntroF, NextGameStep](CreateCharacter)

      case RestoreGame =>
        (messageSource, restore)
          .mapN { (src, gameProgress) =>
            gameProgress
              .map(gp => welcomeBack(gp, src))
              .getOrElse(pure[ShowIntroF, NextGameStep](CreateCharacter))
          }
          .flatMap(identity)

      case QuitGame =>
        pure[ShowIntroF, NextGameStep](GameOver)
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

  private def welcomeBack(gp: GameProgress, src: LocalizedMessageSource): Free[ShowIntroF, NextGameStep] = {
    val name: String = gp.mainCharacter.name
    val experience: Int = gp.mainCharacterExperience
    getMessage(src, "welcome-back", name, experience.toString).map(_ => Explore(gp))
  }
}

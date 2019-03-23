package joguin.game.step

import cats.data.EitherK
import cats.free.Free
import cats.free.Free._
import cats.implicits._
import joguin.game.progress.{GameProgress, GameProgressRepository, GameProgressRepositoryOp}
import joguin.playerinteraction.interaction.{Interaction, InteractionOp}
import joguin.playerinteraction.message.{LocalizedMessageSource, Messages, MessagesOp, ShowIntroMessageSource}
import joguin.game.step.ShowIntroStep._
import eu.timepit.refined.auto._
import joguin.game.step.GameStep.NextGameStep

final class ShowIntroStep(
  implicit I: Interaction[ShowIntroOp],
  M: Messages[ShowIntroOp],
  R: GameProgressRepository[ShowIntroOp]
) {
  import I._, M._, R._, Interaction._

  def start: Free[ShowIntroOp,NextGameStep] = {
    val option: Free[ShowIntroOp,String] = for {
      intro <- message(msrc, "intro")
      _ <- writeMessage(intro)
      hasSavedProgress <- savedProgressExists

      startKey = if (hasSavedProgress) "start-with-resume" else "start"
      startMessage <- message(msrc, startKey)
      errorMessage <- message(msrc, "error-invalid-option")

      option <- ask(
        startMessage,
        errorMessage,
        _.toLowerCase.some,
        validateOptionFn(hasSavedProgress)
      )
    } yield option

    option.flatMap {
      case "n" =>
        pure[ShowIntroOp,NextGameStep](CreateCharacter)

      case "r" =>
        restore.flatMap {
          _.map(gameProgress => welcomeBack(gameProgress))
            .getOrElse(pure[ShowIntroOp,NextGameStep](CreateCharacter))
        }

      case _ =>
        pure[ShowIntroOp,NextGameStep](GameOver)
    }
  }

  private def welcomeBack(gp: GameProgress): Free[ShowIntroOp,NextGameStep] = {
    val name: String = gp.mainCharacter.name
    val experience: Int = gp.mainCharacterExperience

    message(msrc, "welcome-back", name, experience.toString).flatMap { _ =>
      pure[ShowIntroOp,NextGameStep](Explore(gp))
    }
  }

  private def validateOptionFn(hasSavedProgress: Boolean): String => Boolean =
    option =>
      if (hasSavedProgress) {
        option === "n" || option === "r" || option === "q"
      } else {
        option === "n" || option === "q"
      }
}

object ShowIntroStep {
  type Ops1[A] = EitherK[MessagesOp,GameProgressRepositoryOp,A]
  type ShowIntroOp[A] = EitherK[InteractionOp,Ops1,A]

  val msrc: LocalizedMessageSource = LocalizedMessageSource.of(ShowIntroMessageSource)
}

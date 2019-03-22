package joguin.game.step

import cats.data.EitherK
import cats.free.Free
import cats.free.Free._
import cats.implicits._
import joguin.game.progress.{GameProgress, GameProgressRepository, GameProgressRepositoryOp}
import joguin.playerinteraction.interaction.{Interaction, InteractionOp}
import joguin.playerinteraction.message.{LocalizedMessageSource, Messages, MessagesOp, ShowIntroMessageSource}
import eu.timepit.refined.auto._

final class ShowIntroStep {
  type EK[A] = EitherK[MessagesOp,GameProgressRepositoryOp,A]
  type ShowIntroApp[A] = EitherK[InteractionOp,EK,A]

  def start(
    implicit I: Interaction[ShowIntroApp],
      M: Messages[ShowIntroApp],
      R: GameProgressRepository[ShowIntroApp]
  ): Free[ShowIntroApp,NextGameStep] = {
    import I._, M._, R._, Interaction._

    val option = for {
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
        pure(NextGameStep(CreateCharacter))

      case "r" =>
        restore.flatMap {
          _.map(gameProgress => welcomeBack(gameProgress))
            .getOrElse(pure(NextGameStep(CreateCharacter)))
        }

      case _ =>
        pure(NextGameStep(GameOver))
    }
  }

  private def welcomeBack(gp: GameProgress)(implicit M: Messages[ShowIntroApp]): Free[ShowIntroApp,NextGameStep] = {
    val name: String = gp.mainCharacter.name
    val experience: Int = gp.mainCharacterExperience

    M.message(msrc, "welcome-back", name, experience.toString).flatMap { _ =>
      pure(NextGameStep(Explore(gp)))
    }
  }

  private def validateOptionFn(hasSavedProgress: Boolean): String => Boolean =
    if (hasSavedProgress) {
      option => option === "n" || option === "r" || option === "q"
    } else {
      option => option === "n" || option === "q"
    }

  private val msrc = LocalizedMessageSource.of(ShowIntroMessageSource)
}

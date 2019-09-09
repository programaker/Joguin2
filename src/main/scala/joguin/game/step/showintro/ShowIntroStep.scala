package joguin.game.step.showintro

import cats.free.Free
import cats.free.Free._
import cats.implicits._
import eu.timepit.refined.auto._
import joguin.game.progress.GameProgress
import joguin.game.progress.GameProgressRepositoryOps
import joguin.game.step.CreateCharacter
import joguin.game.step.Explore
import joguin.game.step.GameOver
import joguin.game.step.GameStep
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.LocalizedMessageSource
import joguin.playerinteraction.message.MessageSourceOps
import joguin.playerinteraction.message.MessagesOps
import joguin.playerinteraction.message.ShowIntroMessageSource

final class ShowIntroStep[F[_]](
  implicit
  i: InteractionOps[F],
  m: MessagesOps[F],
  s: MessageSourceOps[F],
  r: GameProgressRepositoryOps[F]
) {
  import ShowIntroMessageSource._
  import i._
  import m._
  import r._
  import s._

  def play: Free[F, GameStep] = {
    val messageSource = getLocalizedMessageSource(ShowIntroMessageSource)

    val answer = for {
      src              <- messageSource
      introMessage     <- getMessage(src)(intro)
      _                <- writeMessage(introMessage)
      hasSavedProgress <- savedProgressExists

      startKey = if (hasSavedProgress) start_with_resume else start_no_resume
      startMessage <- getMessage(src)(startKey)
      errorMessage <- getMessage(src)(error_invalid_option)

      answer <- ask(
        startMessage,
        errorMessage,
        ShowIntroOption.parse(_, hasSavedProgress)
      )
    } yield answer

    answer.flatMap {
      case NewGame =>
        pure[F, GameStep](CreateCharacter)

      case RestoreGame =>
        (messageSource, restore).mapN { (src, gameProgress) =>
          gameProgress
            .map(welcomeBack(_, src))
            .getOrElse(pure[F, GameStep](CreateCharacter))
        }.flatten

      case QuitGame =>
        pure[F, GameStep](GameOver)
    }
  }

  private def welcomeBack(
    gp: GameProgress,
    src: LocalizedMessageSource[ShowIntroMessageSource.type]
  ): Free[F, GameStep] = {

    val name: String = gp.mainCharacter.name
    val experience: Int = gp.mainCharacter.experience
    getMessageFmt(src)(welcome_back, List(name, experience.toString)).map(_ => Explore(gp))
  }
}

object ShowIntroStep {
  def apply[F[_]](
    implicit
    i: InteractionOps[F],
    m: MessagesOps[F],
    s: MessageSourceOps[F],
    r: GameProgressRepositoryOps[F]
  ): ShowIntroStep[F] =
    new ShowIntroStep[F]
}

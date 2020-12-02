package joguin.game.step

import cats.free.Free
import cats.free.Free._
import cats.free.Free.pure
import cats.syntax.apply._
import cats.syntax.flatMap._
import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import eu.timepit.refined.string.MatchesRegex
import joguin.game.progress._
import joguin.game.step.GameStep._
import joguin.game.step.showintro.ShowIntroOption._
import joguin.playerinteraction.interaction.ask
import joguin.playerinteraction.message.LocalizedMessageSource
import joguin.playerinteraction.message.MessageSource.ShowIntroMessageSource
import joguin.playerinteraction.message.MessageSource.ShowIntroMessageSource._
import joguin.playerinteraction.message.MessagesOps

package object showintro {
  type LocalizedShowIntroMessageSource = LocalizedMessageSource[ShowIntroMessageSource.type]

  type ShowIntroOptionR = MatchesRegex["^[nqr]$"]
  type ShowIntroOptionNoRestoreR = MatchesRegex["^[nq]$"]

  def playShowIntroStep[F[_]](implicit env: ShowIntroStepEnv[F]): Free[F, GameStep] = {
    import env._
    import gameProgressRepositoryOps._
    import interactionOps._
    import messageOps._

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
        parseShowIntroOption(_, hasSavedProgress)
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

  private def parseShowIntroOption(s: String, hasSavedProgress: Boolean): Option[ShowIntroOption] = {
    val sanitizedS = s.toLowerCase()

    val refAnswer =
      if (hasSavedProgress) {
        refineV[ShowIntroOptionR](sanitizedS)
      } else {
        refineV[ShowIntroOptionNoRestoreR](sanitizedS)
      }

    refAnswer.toOption.map(_.value match {
      case "n" => NewGame
      case "q" => QuitGame
      case "r" => RestoreGame
    })
  }

  private def welcomeBack[F[_]](gp: GameProgress, src: LocalizedShowIntroMessageSource)(
    implicit m: MessagesOps[F]
  ): Free[F, GameStep] = {

    val name: String = CharacterNameField.get(gp)
    val experience: Int = ExperienceField.get(gp)
    m.getMessageFmt(src)(welcome_back, List(name, experience.toString)).map(_ => Explore(gp))
  }
}

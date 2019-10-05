package joguin.game.step

import cats.free.Free
import cats.free.Free._
import cats.free.Free.pure
import cats.implicits._
import eu.timepit.refined.W
import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import eu.timepit.refined.string.MatchesRegex
import joguin.game.progress.GameProgress
import joguin.game.progress.GameProgressRepositoryOps
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.interaction.ask
import joguin.playerinteraction.message.LocalizedMessageSource
import joguin.playerinteraction.message.MessagesOps
import joguin.playerinteraction.message.ShowIntroMessageSource
import joguin.playerinteraction.message.ShowIntroMessageSource.welcome_back

package object showintro {
  type ShowIntroOptionR = MatchesRegex[W.`"""^[nqr]$"""`.T]
  type ShowIntroOptionNoRestoreR = MatchesRegex[W.`"""^[nq]$"""`.T]

  def playShowIntroStep[F[_]](
    implicit
    i: InteractionOps[F],
    m: MessagesOps[F],
    r: GameProgressRepositoryOps[F]
  ): Free[F, GameStep] = {
    import ShowIntroMessageSource._
    import i._
    import m._
    import r._

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

  private def welcomeBack[F[_]](
    gp: GameProgress,
    src: LocalizedMessageSource[ShowIntroMessageSource.type]
  )(implicit m: MessagesOps[F]): Free[F, GameStep] = {

    val name: String = gp.mainCharacter.name
    val experience: Int = gp.mainCharacter.experience
    m.getMessageFmt(src)(welcome_back, List(name, experience.toString)).map(_ => Explore(gp))
  }
}

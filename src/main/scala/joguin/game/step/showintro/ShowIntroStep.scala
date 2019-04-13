package joguin.game.step.showintro

import cats.free.Free
import cats.free.Free._
import cats.implicits._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.game.progress.{GameProgress, GameProgressRepositoryOps}
import joguin.game.step.{CreateCharacter, Explore, GameOver, GameStep}
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.{
  LocalizedMessageSource, 
  MessageSourceOps, 
  MessagesOps, 
  ShowIntroMessageSource
}

final class ShowIntroStep(
  implicit i: InteractionOps[ShowIntroF],
  m: MessagesOps[ShowIntroF],
  s: MessageSourceOps[ShowIntroF],
  r: GameProgressRepositoryOps[ShowIntroF]
) {
  import i._
  import m._
  import r._
  import s._
  import ShowIntroMessageSource._

  def start: Free[ShowIntroF, GameStep] = {
    val messageSource = getLocalizedMessageSource(ShowIntroMessageSource)

    val answer = for {
      src <- messageSource
      introMessage <- getMessage(src)(intro)
      _ <- writeMessage(introMessage)
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
        pure[ShowIntroF, GameStep](CreateCharacter)

      case RestoreGame =>
        (messageSource, restore).mapN { (src, gameProgress) =>
          gameProgress
            .map(welcomeBack(_, src))
            .getOrElse(pure[ShowIntroF, GameStep](CreateCharacter))
        }.flatten

      case QuitGame =>
        pure[ShowIntroF, GameStep](GameOver)
    }
  }

  private def welcomeBack(
    gp: GameProgress,
    src: LocalizedMessageSource[ShowIntroMessageSource.type]
  ): Free[ShowIntroF, GameStep] = {

    val name: String = gp.mainCharacter.name
    val experience: Int = gp.mainCharacterExperience
    getMessageFmt(src)(welcome_back, List(name, experience.toString)).map(_ => Explore(gp))
  }
}

sealed trait ShowIntroOption
case object NewGame extends ShowIntroOption
case object RestoreGame extends ShowIntroOption
case object QuitGame extends ShowIntroOption

object ShowIntroOption {
  def parse(s: String, hasSavedProgress: Boolean): Option[ShowIntroOption] = {
    val sanitizedS = s.toLowerCase()

    val refAnswer =
      if (hasSavedProgress) {
        refineV[ShowIntroOptions](sanitizedS)
      } else {
        refineV[ShowIntroOptionsNoRestore](sanitizedS)
      }

    refAnswer.toOption.map(_.value match {
      case "n" => NewGame
      case "q" => QuitGame
      case "r" => RestoreGame
    })
  }
}

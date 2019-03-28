package joguin.game.step.explore

import cats.free.Free
import cats.free.Free._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.alien.Invasion
import joguin.game.progress.{GameProgress, Index}
import joguin.game.step.GameStepOps.NextGameStep
import joguin.game.step.{Fight, GameOver, Quit}
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.{ExploreMessageSource, LocalizedMessageSource, MessageSourceOps, MessagesOps}
import joguin.playerinteraction.wait.WaitOps

import scala.concurrent.duration._

sealed trait ExploreAnswer
object QuitGame extends ExploreAnswer
final case class GoToInvasion(index: Index) extends ExploreAnswer

final class ExploreStep(
  implicit s: MessageSourceOps[ExploreF],
  m: MessagesOps[ExploreF],
  i: InteractionOps[ExploreF],
  w: WaitOps[ExploreF]
) {
  import i._
  import m._
  import s._
  import w._

  def start(gameProgress: GameProgress): Free[ExploreF, NextGameStep] = {
    for {
      _ <- writeMessage("\n")
      src <- getLocalizedMessageSource(ExploreMessageSource)

      _ <- showInvasions(gameProgress.invasions, gameProgress.defeatedInvasionsTrack, src, 1)

      nextStep <- if (gameProgress.allInvasionsDefeated) {
        missionAcomplished(src)
      } else {
        chooseYourDestiny(src, gameProgress)
      }
    } yield nextStep
  }

  private def showInvasions(
    invasions: List[Invasion],
    defeatedInvasions: Set[Index],
    src: LocalizedMessageSource,
    index: Index
  ): Free[ExploreF, Unit] = invasions match {
    case Nil =>
      pure(())

    case head :: tail =>
      showInvasion(
        head,
        defeatedInvasions.contains(index),
        src,
        index
      ).flatMap(_ => showInvasions(tail, defeatedInvasions, src, index + 1))
  }

  private def showInvasion(
    invasion: Invasion,
    invasionDefeated: Boolean,
    src: LocalizedMessageSource,
    index: Index
  ): Free[ExploreF, Unit] = {
    val city = invasion.city

    val key = if (invasionDefeated) {
      "human-dominated-city"
    } else {
      "alien-dominated-city"
    }

    getMessageFmt(src, key, List(index.toString, city.name, city.country)).flatMap(writeMessage)
  }

  private def missionAcomplished(src: LocalizedMessageSource): Free[ExploreF, NextGameStep] =
    for {
      message <- getMessage(src, "mission-accomplished")
      _ <- writeMessage(message)
      _ <- waitFor(10.seconds)
    } yield GameOver

  private def chooseYourDestiny(src: LocalizedMessageSource, gp: GameProgress): Free[ExploreF, NextGameStep] = {
    //TODO -> store the size as a GameProgress state
    val invasionCount: Index = gp.invasions.size

    for {
      message <- getMessageFmt(src, "where-do-you-want-to-go", List("1", invasionCount.value.toString))
      errorMessage <- getMessage(src, "error-invalid-option")

      answer <- ask(
        message,
        errorMessage,
        parseAnswer(_, invasionCount)
      )
    } yield
      answer match {
        case QuitGame => Quit(gp)
        case GoToInvasion(index) => Fight(gp, index)
      }
  }

  private def parseAnswer(answer: String, invasionCount: Index): Option[ExploreAnswer] =
    refineV[IndexOrQuit](answer).toOption
      .map(_.value.toLowerCase)
      .flatMap {
        case "q" =>
          Some(QuitGame)
        case index =>
          Some(index.toInt).filter(_ <= invasionCount).map(GoToInvasion(_))
      }
}

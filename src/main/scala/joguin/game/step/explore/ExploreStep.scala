package joguin.game.step.explore

import cats.free.Free
import cats.free.Free._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.alien.Invasion
import joguin.game.progress.{Count, GameProgress, Index, IndexR}
import joguin.game.step.{Fight, GameOver, GameStep, Quit}
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.{ExploreMessageSource, LocalizedMessageSource, MessageSourceOps, MessagesOps}
import joguin.playerinteraction.wait.WaitOps

import scala.concurrent.duration._

final class ExploreStep[F[_]](
  implicit
  s: MessageSourceOps[F],
  m: MessagesOps[F],
  i: InteractionOps[F],
  w: WaitOps[F]
) {
  import ExploreMessageSource._
  import i._
  import m._
  import s._
  import w._

  def start(gameProgress: GameProgress): Free[F, GameStep] = {
    for {
      _ <- writeMessage("\n")
      src <- getLocalizedMessageSource(ExploreMessageSource)
      _ <- showInvasions(gameProgress.invasions, gameProgress.defeatedInvasionsTrack, src, Some(1))

      nextStep <- if (gameProgress.allInvasionsDefeated) {
        missionAccomplished(src)
      } else {
        chooseYourDestiny(src, gameProgress)
      }
    } yield nextStep
  }

  private def showInvasions(
    invasions: List[Invasion],
    defeatedInvasions: Set[Index],
    src: LocalizedMessageSource[ExploreMessageSource.type],
    index: Option[Index]
  ): Free[F, Unit] = (invasions, index) match {

    case (Nil, _) =>
      pure(())

    case (_, None) =>
      pure(()) //A very improbable refinement error happened

    case (invasion :: otherInvasions, Some(idx)) =>
      showInvasion(invasion, defeatedInvasions.contains(idx), src, idx)
        .flatMap { _ =>
          showInvasions(otherInvasions, defeatedInvasions, src, refineV[IndexR](idx + 1).toOption)
        }
  }

  private def showInvasion(
    invasion: Invasion,
    invasionDefeated: Boolean,
    src: LocalizedMessageSource[ExploreMessageSource.type],
    index: Index
  ): Free[F, Unit] = {

    val key = if (invasionDefeated) {
      human_dominated_city
    } else {
      alien_dominated_city
    }

    val city = invasion.city
    getMessageFmt(src)(key, List(index.toString, city.name, city.country)).flatMap(writeMessage)
  }

  private def missionAccomplished(src: LocalizedMessageSource[ExploreMessageSource.type]): Free[F, GameStep] =
    for {
      message <- getMessage(src)(mission_accomplished)
      _ <- writeMessage(message)
      _ <- waitFor(10.seconds)
    } yield GameOver

  private def chooseYourDestiny(
    src: LocalizedMessageSource[ExploreMessageSource.type],
    gp: GameProgress
  ): Free[F, GameStep] = {

    val invasionCount = gp.invasionCount

    for {
      message <- getMessageFmt(src)(where_do_you_want_to_go, List("1", invasionCount.value.toString))
      errorMessage <- getMessage(src)(error_invalid_option)
      option <- ask(message, errorMessage, ExploreOption.parse(_, invasionCount))
    } yield option match {
      case QuitGame => Quit(gp)
      case GoToInvasion(index) => Fight(gp, index)
    }
  }
}
object ExploreStep {
  def apply[F[_]](
    implicit
    s: MessageSourceOps[F],
    m: MessagesOps[F],
    i: InteractionOps[F],
    w: WaitOps[F]
  ): ExploreStep[F] = {
    new ExploreStep[F]
  }
}


sealed trait ExploreOption
object QuitGame extends ExploreOption
final case class GoToInvasion(index: Index) extends ExploreOption
object ExploreOption {
  def parse(s: String, invasionCount: Count): Option[ExploreOption] =
    refineV[ExploreOptionR](s.toLowerCase).toOption
      .map(_.value)
      .flatMap {
        case "q" =>
          Some(QuitGame)
        case index =>
          Some(index.toInt)
            .map(refineV[IndexR](_))
            .flatMap(_.toOption)
            .filter(_ <= invasionCount.value)
            .map(GoToInvasion)
      }
}

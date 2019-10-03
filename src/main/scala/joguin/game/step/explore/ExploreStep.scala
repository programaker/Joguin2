package joguin.game.step.explore

import cats.free.Free
import cats.free.Free._
import cats.implicits._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.alien.Invasion
import joguin.game.progress.Count
import joguin.game.progress.CountR
import joguin.game.progress.GameProgress
import joguin.game.progress.Index
import joguin.game.progress.IndexR
import joguin.game.progress.allInvasionsDefeated
import joguin.game.step.Fight
import joguin.game.step.GameOver
import joguin.game.step.GameStep
import joguin.game.step.Quit
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.ExploreMessageSource
import joguin.playerinteraction.message.LocalizedMessageSource
import joguin.playerinteraction.message.MessageSourceOps
import joguin.playerinteraction.message.MessagesOps
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

  def play(gameProgress: GameProgress): Free[F, GameStep] =
    for {
      _   <- writeMessage("\n")
      src <- getLocalizedMessageSource(ExploreMessageSource)
      _   <- showInvasions(gameProgress.invasions, gameProgress, src, Some(1))

      nextStep <- if (allInvasionsDefeated(gameProgress)) {
        missionAccomplished(src)
      } else {
        chooseOption(src, gameProgress)
      }
    } yield nextStep

  private def showInvasions(
    invasions: Vector[Invasion],
    gameProgress: GameProgress,
    src: LocalizedMessageSource[ExploreMessageSource.type],
    index: Option[Index]
  ): Free[F, Unit] =
    (invasions.headOption, index)
      .mapN { (invasion, idx) =>
        showInvasion(invasion, gameProgress, src, idx)
          .flatMap { _ =>
            showInvasions(invasions.drop(1), gameProgress, src, refineV[IndexR](idx + 1).toOption)
          }
      }
      .getOrElse(pure(()))

  private def showInvasion(
    invasion: Invasion,
    gameProgress: GameProgress,
    src: LocalizedMessageSource[ExploreMessageSource.type],
    idx: Index
  ): Free[F, Unit] = {

    //TODO => Move "isInvasionDefeated" to somewhere else, like a HumanArmy report
    val key = if (gameProgress.defeatedInvasionsTrack.contains(idx)) {
      human_dominated_city
    } else {
      alien_dominated_city
    }

    getMessageFmt(src)(key, List(idx.toString, invasion.city.name, invasion.city.country))
      .flatMap(writeMessage)
  }

  private def missionAccomplished(src: LocalizedMessageSource[ExploreMessageSource.type]): Free[F, GameStep] =
    for {
      message <- getMessage(src)(mission_accomplished)
      _       <- writeMessage(message)
      _       <- waitFor(10.seconds)
    } yield GameOver

  private def chooseOption(
    src: LocalizedMessageSource[ExploreMessageSource.type],
    gp: GameProgress
  ): Free[F, GameStep] = {

    val invasionCount = refineV[CountR](gp.invasions.size).getOrElse(0: Count)

    for {
      message      <- getMessageFmt(src)(where_do_you_want_to_go, List("1", invasionCount.toString))
      errorMessage <- getMessage(src)(error_invalid_option)
      option       <- ask(message, errorMessage, parseExploreOption(_, invasionCount))
    } yield option match {
      case QuitGame            => Quit(gp)
      case GoToInvasion(index) => Fight(gp, index)
    }
  }
}

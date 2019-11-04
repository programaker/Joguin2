package joguin.game.step.explore

import cats.free.Free
import cats.free.Free._
import cats.implicits._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.IdxSeq
import joguin.alien.invasion.Invasion
import joguin.game.progress.Count
import joguin.game.progress.CountR
import joguin.game.progress.GameProgress
import joguin.game.progress.Index
import joguin.game.progress.IndexR
import joguin.game.progress._
import joguin.game.progress.allInvasionsDefeated
import joguin.game.step.GameStep
import joguin.game.step.GameStep._
import joguin.game.step.explore.ExploreOption.GoToInvasion
import joguin.game.step.explore.ExploreOption.QuitGame
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.interaction._
import joguin.playerinteraction.message.MessageSource.ExploreMessageSource
import joguin.playerinteraction.message.MessagesOps
import joguin.playerinteraction.wait.WaitOps

import scala.concurrent.duration._

final class ExploreStep[F[_]](
  implicit
  m: MessagesOps[F],
  i: InteractionOps[F],
  w: WaitOps[F]
) {
  import ExploreMessageSource._
  import i._
  import m._
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
    invasions: IdxSeq[Invasion],
    gameProgress: GameProgress,
    src: LocalizedExploreMessageSource,
    index: Option[Index]
  ): Free[F, Unit] =
    (invasions.headOption, index)
      .mapN { (invasion, idx) =>
        val key = if (isInvasionDefeated(gameProgress, idx)) {
          human_dominated_city
        } else {
          alien_dominated_city
        }

        getMessageFmt(src)(key, List(idx.toString, invasion.city.name, invasion.city.country))
          .flatMap(writeMessage)
          .flatMap { _ =>
            showInvasions(invasions.drop(1), gameProgress, src, refineV[IndexR](idx + 1).toOption)
          }
      }
      .getOrElse(pure(()))

  private def missionAccomplished(src: LocalizedExploreMessageSource): Free[F, GameStep] =
    for {
      message <- getMessage(src)(mission_accomplished)
      _       <- writeMessage(message)
      _       <- waitFor(10.seconds)
    } yield GameOver

  private def chooseOption(src: LocalizedExploreMessageSource, gp: GameProgress): Free[F, GameStep] = {
    val invasionCount = refineV[CountR](gp.invasions.size).getOrElse(0: Count)

    for {
      message      <- getMessageFmt(src)(where_do_you_want_to_go, List("1", invasionCount.toString))
      errorMessage <- getMessage(src)(error_invalid_option)
      option       <- ask(message, errorMessage, parseExploreOption(_, invasionCount))
    } yield
      option match {
        case QuitGame            => Quit(gp)
        case GoToInvasion(index) => Fight(gp, index)
      }
  }
}

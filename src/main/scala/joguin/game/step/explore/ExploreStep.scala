package joguin.game.step.explore

import cats.free.Free
import cats.free.Free._
import cats.syntax.foldable._
import eu.timepit.refined._
import joguin.refined.auto._
import joguin.IdxSeq
import joguin.alien.invasion._
import joguin.game.progress.Count
import joguin.game.progress.CountR
import joguin.game.progress.GameProgress
import joguin.game.progress.allInvasionsDefeated
import joguin.game.step.GameStep
import joguin.game.step.GameStep._
import joguin.game.step.explore.ExploreOption.GoToInvasion
import joguin.game.step.explore.ExploreOption.QuitGame
import joguin.playerinteraction.interaction._
import joguin.playerinteraction.message.MessageSource.ExploreMessageSource
import joguin.playerinteraction.message.MessageSource.ExploreMessageSource._

import scala.concurrent.duration._

final class ExploreStep[F[_]](implicit env: ExploreStepEnv[F]) {
  import env._
  import interactionOps._
  import messageOps._
  import waitOps._

  def play(gameProgress: GameProgress): Free[F, GameStep] =
    for {
      _   <- writeMessage("\n")
      src <- getLocalizedMessageSource(ExploreMessageSource)
      _   <- showInvasions(gameProgress.invasions, src)

      nextStep <- if (allInvasionsDefeated(gameProgress)) {
        missionAccomplished(src)
      } else {
        chooseOption(gameProgress, src)
      }
    } yield nextStep

  private def showInvasions(invasions: IdxSeq[Invasion], src: LocalizedExploreMessageSource): Free[F, Unit] = {
    val zero = ((), 1)

    val folded = invasions.foldLeftM(zero) { (tuple, invasion) =>
      val key = if (invasion.defeated) {
        human_dominated_city
      } else {
        alien_dominated_city
      }

      val (_, idx) = tuple
      val args: List[String] = List(idx.toString, CityNameField.get(invasion), CountryField.get(invasion))
      getMessageFmt(src)(key, args).flatMap(writeMessage).map((_, idx + 1))
    }

    folded.map(_._1)
  }

  private def missionAccomplished(src: LocalizedExploreMessageSource): Free[F, GameStep] =
    for {
      message <- getMessage(src)(mission_accomplished)
      _       <- writeMessage(message)
      _       <- waitFor(10.seconds)
    } yield GameOver

  private def chooseOption(gp: GameProgress, src: LocalizedExploreMessageSource): Free[F, GameStep] = {
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

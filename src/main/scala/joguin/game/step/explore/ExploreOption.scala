package joguin.game.step.explore

import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.game.progress.Count
import joguin.game.progress.Index
import joguin.game.progress.IndexR

sealed abstract class ExploreOption extends Product with Serializable
case object QuitGame extends ExploreOption
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

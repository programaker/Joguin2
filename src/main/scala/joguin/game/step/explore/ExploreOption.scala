package joguin.game.step.explore

import joguin.game.progress.Index

sealed abstract class ExploreOption extends Product with Serializable
case object QuitGame extends ExploreOption
final case class GoToInvasion(index: Index) extends ExploreOption

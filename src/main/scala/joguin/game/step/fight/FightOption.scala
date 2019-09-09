package joguin.game.step.fight

import eu.timepit.refined.refineV

sealed abstract class FightOption extends Product with Serializable
case object FightAliens extends FightOption
case object Retreat extends FightOption

object FightOption {
  def parse(s: String): Option[FightOption] =
    refineV[FightOptionR](s.toLowerCase).toOption
      .map(_.value)
      .map {
        case "f" => FightAliens
        case "r" => Retreat
      }
}

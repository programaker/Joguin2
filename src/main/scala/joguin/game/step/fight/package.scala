package joguin.game.step

import eu.timepit.refined.W
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.refineV
import joguin.game.step.fight.FightOption.FightAliens
import joguin.game.step.fight.FightOption.Retreat

package object fight {
  type FightOptionR = Equal[W.`"f"`.T] Or Equal[W.`"r"`.T]

  def parseFightOption(s: String): Option[FightOption] =
    refineV[FightOptionR](s.toLowerCase).toOption
      .map(_.value match {
        case "f" => FightAliens
        case "r" => Retreat
      })
}

package joguin.game.step

import eu.timepit.refined.W
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal

package object fight {
  type FightOptionR = Equal[W.`"f"`.T] Or Equal[W.`"r"`.T]
}

package joguin.testutil.generator

import eu.timepit.refined.refineV
import joguin.alien.{AlienArmy, Power, PowerR}
import org.scalacheck.Gen

object PowerGenerators {
  def genPower: Gen[Power] = {
    val elsePower: Power = AlienArmy.minPower

    Gen.choose(min = AlienArmy.minPower.value, max = AlienArmy.maxPower.value)
      .map(refineV[PowerR](_))
      .map(_.getOrElse(elsePower))
  }

  def genInvalidPower: Gen[Int] =
    Gen.choose(-AlienArmy.minPower.value, 0)
}

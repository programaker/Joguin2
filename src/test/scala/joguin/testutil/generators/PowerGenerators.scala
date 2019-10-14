package joguin.testutil.generators

import eu.timepit.refined.refineV
import joguin.alien._
import joguin.alien.Power
import joguin.alien.PowerR
import org.scalacheck.Gen

object PowerGenerators {
  def genPower: Gen[Power] =
    Gen
      .choose(min = MinPower.value, max = MaxPower.value)
      .map(refineV[PowerR](_))
      .map(_.getOrElse(MinPower: Power))

  def genInvalidPower: Gen[Int] =
    Gen.choose(-MinPower.value, 0)
}

package joguin.testutil.generators

import joguin.refined.auto._
import eu.timepit.refined.refineV
import joguin.alien.MaxPower
import joguin.alien.MinPower
import joguin.alien.Power
import joguin.alien.PowerR
import org.scalacheck.Gen

package object powergenerator {
  def genValidPower: Gen[Power] =
    Gen
      .choose(min = MinPower: Int, max = MaxPower: Int)
      .map(refineV[PowerR](_))
      .map(_.getOrElse(MinPower))

  def genInvalidPower: Gen[Int] = Gen.choose(-MinPower, 0)
}

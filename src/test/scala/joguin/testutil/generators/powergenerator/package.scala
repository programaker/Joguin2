package joguin.testutil.generators

import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.alien.MaxPower
import joguin.alien.MinPower
import joguin.alien.Power
import joguin.alien.PowerR
import org.scalacheck.Gen

package object powergenerator {
  def genPower: Gen[Power] =
    Gen
      .choose(min = MinPower: Int, max = MaxPower: Int)
      .map(refineV[PowerR](_))
      .map(_.getOrElse(MinPower))

  def genInvalidPower: Gen[Int] = Gen.choose(-MinPower, 0)
}

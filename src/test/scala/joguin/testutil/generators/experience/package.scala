package joguin.testutil.generators

import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.earth.maincharacter.Experience
import joguin.earth.maincharacter.ExperienceR
import org.scalacheck.Gen

package object experience {
  def genExperience: Gen[Experience] =
    Gen
      .choose(min = 0, max = 20000)
      .map(refineV[ExperienceR](_))
      .map(_.getOrElse(0))

  def genInvalidExperience: Gen[Int] =
    Gen.choose(Int.MinValue, -1)
}

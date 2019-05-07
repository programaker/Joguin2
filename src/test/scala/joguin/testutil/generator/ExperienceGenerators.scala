package joguin.testutil.generator

import eu.timepit.refined.refineV
import eu.timepit.refined.auto._
import joguin.game.progress.{Experience, ExperienceR}
import org.scalacheck.Gen

object ExperienceGenerators {
  def genExperience: Gen[Experience] = {
    val elseExperience: Experience = 0

    Gen.choose(min = 0, max = 20000)
      .map(refineV[ExperienceR](_))
      .map(_.getOrElse(elseExperience))
  }

  def genInvalidExperience: Gen[Int] =
    Gen.choose(Int.MinValue, -1)
}

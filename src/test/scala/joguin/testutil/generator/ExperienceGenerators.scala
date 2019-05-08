package joguin.testutil.generator

import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.game.progress.Experience
import joguin.game.progress.ExperienceR
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

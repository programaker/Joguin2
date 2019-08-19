package joguin.testutil.generator

import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.earth.maincharacter.Age
import joguin.earth.maincharacter.AgeR
import org.scalacheck.Gen

object AgeGenerators {
  def genAge: Gen[Age] =
    Gen
      .choose(min = 18, max = 65)
      .map(refineV[AgeR](_))
      .map(_.getOrElse(18: Age))

  def genInvalidAge: Gen[Int] =
    Gen.choose(min = -17, max = 17)
}

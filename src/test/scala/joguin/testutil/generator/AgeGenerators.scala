package joguin.testutil.generator

import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.earth.maincharacter.Age
import joguin.earth.maincharacter.AgeR
import org.scalacheck.Gen

object AgeGenerators {
  def genAge: Gen[Age] = {
    val elseAge: Age = 18

    Gen
      .choose(min = 18, max = 65)
      .map(refineV[AgeR](_))
      .map(_.getOrElse(elseAge))
  }

  def genInvalidAge: Gen[Int] =
    Gen.choose(min = -17, max = 17)
}

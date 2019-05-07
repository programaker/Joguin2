package joguin.testutil.generator

import eu.timepit.refined.refineV
import eu.timepit.refined.auto._
import joguin.earth.maincharacter.{Age, AgeR}
import org.scalacheck.Gen

object AgeGenerators {
  def genAge: Gen[Age] = {
    val elseAge: Age = 18

    Gen.choose(min = 18, max = 65)
      .map(refineV[AgeR](_))
      .map(_.getOrElse(elseAge))
  }

  def genInvalidAge: Gen[Int] =
    Gen.choose(min = -17, max = 17)
}

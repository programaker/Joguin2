package joguin.testutil.generators

import eu.timepit.refined.refineV
import joguin.earth.maincharacter.Age
import joguin.earth.maincharacter.AgeR
import org.scalacheck.Gen
import eu.timepit.refined.auto._
import org.scalacheck.Arbitrary

package object age {
  implicit val validAge: Arbitrary[Age] = Arbitrary(genValidAge)
  implicit val invalidAge: Arbitrary[Int] = Arbitrary(genInvalidAge)

  def genValidAge: Gen[Age] =
    Gen
      .choose(min = 18, max = 65)
      .map(refineV[AgeR](_))
      .map(_.getOrElse(18))

  def genInvalidAge: Gen[Int] =
    Gen.choose(min = -17, max = 17)
}

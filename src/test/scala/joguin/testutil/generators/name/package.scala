package joguin.testutil.generators

import joguin.refined.auto._
import eu.timepit.refined.refineV
import joguin.Name
import joguin.NameR
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

package object name {
  implicit val validName: Arbitrary[Name] = Arbitrary(genValidName)
  implicit val invalidName: Arbitrary[String] = Arbitrary(genInvalidName)

  def genValidName: Gen[Name] =
    Gen.alphaStr
      .map(refineV[NameR](_))
      .map(_.getOrElse("Fallback Name"))

  def genInvalidName: Gen[String] = Gen.oneOf("", " ", "   ")
}

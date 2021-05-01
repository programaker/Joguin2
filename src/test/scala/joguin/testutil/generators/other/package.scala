package joguin.testutil.generators

import org.scalacheck.Arbitrary
import org.scalacheck.Gen

package object other {
  implicit val smallInt: Arbitrary[Int] = Arbitrary(genSmallInt)
  implicit val quitOption: Arbitrary[String] = Arbitrary(genQuitOption)

  def genSmallInt: Gen[Int] =
    Gen.choose(min = 1, max = 100)

  def genQuitOption: Gen[String] =
    Gen.oneOf("q", "Q")

  def arbitraryTag[T, A](gen: Gen[A]): Arbitrary[Tag[T, A]] =
    Arbitrary(gen.map(Tag(_)))
}

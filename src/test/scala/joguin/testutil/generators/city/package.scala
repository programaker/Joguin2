package joguin.testutil.generators

import joguin.earth.city.City
import joguin.testutil.generators.name._
import joguin.testutil.generators.country._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._
import cats.syntax.apply._

package object city {
  implicit val validCity: Arbitrary[City] = Arbitrary(genValidCity)
  implicit val invalidCity: Arbitrary[String] = Arbitrary(genInvalidCity)

  def genValidCity: Gen[City] = (genValidName, genValidCountry).mapN(City)

  def genInvalidCity: Gen[String] = genInvalidName
}

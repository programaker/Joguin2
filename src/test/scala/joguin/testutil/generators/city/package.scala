package joguin.testutil.generators

import joguin.earth.city.City
import joguin.testutil.generators.name._
import joguin.testutil.generators.country._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

package object city {
  implicit val validCity: Arbitrary[City] = Arbitrary(genValidCity)
  implicit val invalidCity: Arbitrary[String] = Arbitrary(genInvalidCity)

  def genValidCity: Gen[City] =
    for {
      name    <- genValidName
      country <- genValidCountry
    } yield City(name, country)

  def genInvalidCity: Gen[String] = genInvalidName
}

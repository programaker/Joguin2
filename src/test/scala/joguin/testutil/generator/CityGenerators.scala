package joguin.testutil.generator

import cats.implicits._
import joguin.earth.city.City
import joguin.testutil.generator.CountryGenerators.genCountry
import joguin.testutil.generator.NameGenerators.genName
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._

object CityGenerators {
  def genCity: Gen[City] =
    (genName, genCountry).mapN(City.apply)

  def genInvalidCity: Gen[String] =
    NameGenerators.genInvalidName
}

package joguin.testutil.generator

import joguin.earth.city.City
import joguin.testutil.generator.CountryGenerators.genCountry
import joguin.testutil.generator.NameGenerators.genName
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._
import cats.implicits._

object CityGenerators {
  def genCity: Gen[City] =
    (genName, genCountry).mapN(City.apply)

  def genInvalidCity: Gen[String] =
    NameGenerators.genInvalidName
}

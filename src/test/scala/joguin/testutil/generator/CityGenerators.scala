package joguin.testutil.generator

import joguin.earth.city.City
import joguin.testutil.generator.CountryGenerators.genCountry
import joguin.testutil.generator.NameGenerators.genName
import org.scalacheck.Gen

object CityGenerators {
  def genCity: Gen[City] =
    for {
      name    <- genName
      country <- genCountry
    } yield City(name, country)

  def genInvalidCity: Gen[String] =
    NameGenerators.genInvalidName
}

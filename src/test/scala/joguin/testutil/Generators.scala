package joguin.testutil

import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.{Name, NameR}
import joguin.earth.{Country, CountryR}
import joguin.earth.city.City
import org.scalacheck.{Arbitrary, Gen}

object Generators {
  implicit val arbCity: Arbitrary[City] = Arbitrary(genCity)

  private def genCity: Gen[City] = {
    for {
      name <- genName
      country <- genCountry
    } yield City(name, country)
  }

  private def genName: Gen[Name] = {
    val fallback: Name = "Fallback Name"
    Gen.alphaStr.map(refineV[NameR](_)).map(_.getOrElse(fallback))
  }

  private def genCountry: Gen[Country] = {
    val fallback: Country = "Fallback Country"
    Gen.alphaStr.map(refineV[CountryR](_)).map(_.getOrElse(fallback))
  }
}

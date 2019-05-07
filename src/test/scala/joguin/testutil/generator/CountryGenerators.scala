package joguin.testutil.generator

import eu.timepit.refined.refineV
import eu.timepit.refined.auto._
import joguin.earth.{Country, CountryR}
import org.scalacheck.Gen

object CountryGenerators {
  def genCountry: Gen[Country] = {
    val elseCountry: Country = "Fallback Country"

    Gen.alphaStr
      .map(refineV[CountryR](_))
      .map(_.getOrElse(elseCountry))
  }

  def genInvalidCountry: Gen[String] =
    NameGenerators.genInvalidName
}

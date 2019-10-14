package joguin.testutil.generators

import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.earth.Country
import joguin.earth.CountryR
import org.scalacheck.Gen

package object country {
  def genCountry: Gen[Country] =
    Gen.alphaStr
      .map(refineV[CountryR](_))
      .map(_.getOrElse("Fallback Country"))

  def genInvalidCountry: Gen[String] = NameGenerators.genInvalidName
}

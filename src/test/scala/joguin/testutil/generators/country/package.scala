package joguin.testutil.generators

import joguin.refined.auto._
import eu.timepit.refined.refineV
import joguin.earth.Country
import joguin.earth.CountryR
import joguin.testutil.generators.name._
import org.scalacheck.Gen

package object country {
  def genValidCountry: Gen[Country] =
    Gen.alphaStr
      .map(refineV[CountryR](_))
      .map(_.getOrElse("Fallback Country"))

  def genInvalidCountry: Gen[String] = genInvalidName
}

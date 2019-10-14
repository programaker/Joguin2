package joguin.testutil.generators

import joguin.earth.city.City
import joguin.testutil.generators.name._
import joguin.testutil.generators.country._
import org.scalacheck.Gen

package object city {
  def genCity: Gen[City] =
    for {
      name    <- genName
      country <- genCountry
    } yield City(name, country)

  def genInvalidCity: Gen[String] = genInvalidName
}

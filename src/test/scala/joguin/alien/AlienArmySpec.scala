package joguin.alien

import cats.Id
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.{Name, NameR}
import joguin.alien.terraformdevice.PowerGeneratorOps._
import joguin.alien.terraformdevice.{PowerGeneratorF, PowerGeneratorInterpreter}
import joguin.earth.{Country, CountryR}
import joguin.earth.city.City
import joguin.testutil.PropertyBasedSpec
import org.scalacheck.{Arbitrary, Gen}

class AlienArmySpec extends PropertyBasedSpec {
  property("attack should put a TerraformDevice in a city with a power between 1000 and 20000") {
    forAll { givenCity: City =>
      val invasion = AlienArmy.attack[PowerGeneratorF](givenCity).foldMap(interpreter)

      invasion.city shouldBe givenCity
      invasion.terraformDevice.defensePower.value should (be >= 1000 and be <= 20000)
    }
  }

  val interpreter: PowerGeneratorInterpreter[Id] = PowerGeneratorInterpreter[Id](liftPower)
  def liftPower(power: =>Power): Id[Power] = power
  implicit val arbCity: Arbitrary[City] = Arbitrary(genCity)

  def genCity: Gen[City] = {
    val fooName: Name = "Foo City"
    val fooCountry: Country = "Foo Country"

    for {
      name <- Gen.alphaStr.map(refineV[NameR](_)).map(_.getOrElse(fooName))
      country <- Gen.alphaStr.map(refineV[CountryR](_)).map(_.getOrElse(fooCountry))
    } yield City(name, country)
  }
}

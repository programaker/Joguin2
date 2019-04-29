package joguin.alien

import joguin.alien.terraformdevice.PowerGeneratorF
import joguin.alien.terraformdevice.PowerGeneratorOps._
import joguin.earth.city.City
import joguin.testutil.Generators._
import joguin.testutil.Interpreters._
import joguin.testutil.PropertyBasedSpec

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
class AlienArmySpec extends PropertyBasedSpec {
  property("attack should put a TerraformDevice in a city with a power between 1000 and 20000") {
    forAll { givenCity: City =>
      val invasion = AlienArmy
        .attack[PowerGeneratorF](givenCity)
        .foldMap(powerGeneratorInterpreter)

      invasion.city shouldBe givenCity
      invasion.terraformDevice.defensePower.value should (be >= 1000 and be <= 20000)
    }
  }
}

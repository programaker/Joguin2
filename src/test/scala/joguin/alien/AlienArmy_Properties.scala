package joguin.alien

import joguin.alien.terraformdevice.PowerGeneratorF
import joguin.alien.terraformdevice.PowerGeneratorOps._
import joguin.earth.city.City
import joguin.testutil.Generators._
import joguin.testutil.Interpreters._
import joguin.testutil.PropertyBasedSpec

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class AlienArmy_Properties extends PropertyBasedSpec {
  property("invading a City produces an Invasion with a TerraformDevice, whose power is between 1000 and 20000") {
    forAll { city: City =>
      val invasion = AlienArmy
        .invade[PowerGeneratorF](city)
        .foldMap(powerGeneratorInterpreter)

      invasion.city shouldBe city
      invasion.terraformDevice.defensePower.value should (be >= 1000 and be <= 20000)
    }
  }
}

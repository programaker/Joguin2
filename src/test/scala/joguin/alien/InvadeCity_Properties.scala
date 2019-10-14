package joguin.alien

import cats.Id
import joguin.alien.terraformdevice.PowerGeneratorF
import joguin.alien.terraformdevice.PowerGeneratorInterpreter
import joguin.alien.terraformdevice.PowerGeneratorOps._
import joguin.earth.city.City
import joguin.testutil.PropertyBasedSpec
import eu.timepit.refined.auto._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class InvadeCity_Properties extends PropertyBasedSpec {
  private val powerGeneratorInterpreter = new PowerGeneratorInterpreter[Id]

  property("invading a City produces an Invasion with a TerraformDevice, whose power is in given range") {
    import joguin.testutil.generators.Generators.city

    forAll { city: City =>
      val invasion = invadeCity[PowerGeneratorF](city, minPower = 1000, maxPower = 20000)
        .foldMap(powerGeneratorInterpreter)

      invasion.city shouldBe city
      invasion.terraformDevice.defensePower.value should (be >= 1000 and be <= 20000)
    }
  }
}

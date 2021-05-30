package joguin.alien

import cats.Id
import joguin.refined.auto._
import joguin.alien.invasion._
import joguin.alien.terraformdevice.PowerGeneratorF
import joguin.alien.terraformdevice.PowerGeneratorInterpreter
import joguin.alien.terraformdevice.PowerGeneratorOps._
import joguin.earth.city.City
import joguin.testutil.PropertyBasedSpec
import joguin.testutil.generators._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class InvadeCity_Properties extends PropertyBasedSpec {
  private val powerGeneratorInterpreter = new PowerGeneratorInterpreter[Id]

  property("invading a City produces an Invasion with a TerraformDevice, whose power is in given range") {
    import city._

    forAll { (city: City) =>
      val invasion = invadeCity[PowerGeneratorF](city, minPower = 1000, maxPower = 20000)
        .foldMap(powerGeneratorInterpreter)

      val generatedPower: Int = DefensePowerField.get(invasion)

      invasion.city shouldBe city
      generatedPower should (be >= 1000 and be <= 20000)
    }
  }
}

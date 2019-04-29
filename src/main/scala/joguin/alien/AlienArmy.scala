package joguin.alien

import cats.free.Free
import eu.timepit.refined.auto._
import joguin.alien.terraformdevice.{PowerGeneratorOps, TerraformDevice}
import joguin.earth.city.City

/** Attacks a city installing a Terraform Device in it, resulting an invasion.
 * Some Terraform Devices are more powerful - and harder to defeat - then others,
 * to make things more interesting */
object AlienArmy {
  val minPower: Power = 1000
  val maxPower: Power = 20000

  def invade[F[_]](city: City)(implicit p: PowerGeneratorOps[F]): Free[F, Invasion] = {
    p.generatePower(minPower, maxPower)
      .map(TerraformDevice)
      .map(Invasion(_, city))
  }
}

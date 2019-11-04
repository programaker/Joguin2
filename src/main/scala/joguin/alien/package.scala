package joguin

import cats.free.Free
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import joguin.alien.invasion
import joguin.alien.invasion.Invasion
import joguin.alien.terraformdevice.PowerGeneratorOps
import joguin.alien.terraformdevice.TerraformDevice
import joguin.earth.city.City

package object alien {
  type PowerR = Positive
  type Power = Int Refined PowerR

  val MinPower: Power = 1000
  val MaxPower: Power = 20000

  /** Attacks a city installing a Terraform Device in it, resulting an invasion.
   * Some Terraform Devices are more powerful - and harder to defeat - then others,
   * to make things more interesting */
  def invadeCity[F[_]](city: City, minPower: Power, maxPower: Power)(
    implicit p: PowerGeneratorOps[F]
  ): Free[F, Invasion] =
    p.generatePower(minPower, maxPower)
      .map(TerraformDevice)
      .map(invasion.Invasion(_, city, defeated = false))
}

package joguin.alien

import cats.free.Free
import eu.timepit.refined.auto._
import joguin.alien.Invasion.InProgress
import joguin.earth.city.City

/** Attacks a city installing a Terraform Device in it, resulting an invasion.
  * The Terraform Devices gain a random defense power to make things more interesting */
object AlienArmy {
  def attack[F[_]](city: City)(implicit P: PowerGenerator[F]): Free[F,Invasion] = {
    import P._

    generatePower(min = 1000, max = 20000)
      .map(TerraformDevice)
      .map(Invasion(_, city, InProgress))
  }
}
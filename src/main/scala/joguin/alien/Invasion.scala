package joguin.alien

import joguin.alien.Invasion.Status
import joguin.earth.city.City

/** The result of an alien army attacking a city.
  *
  * After the invasion, the city is completely dominated by the aliens
  * and got a Terraform Device installed in it */
case class Invasion(
  terraformDevice: TerraformDevice,
  city: City,
  status: Status
)

object Invasion {
  sealed trait Status
  case object InProgress extends Status
  case object Defeated extends Status
}

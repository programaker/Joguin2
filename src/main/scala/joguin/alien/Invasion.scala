package joguin.alien

import joguin.alien.terraformdevice.TerraformDevice
import joguin.earth.city.City

/** The result of an alien army attacking a city.
  *
  * After the invasion, the city is completely dominated by the aliens
  * and got a Terraform Device installed in it */
final case class Invasion(terraformDevice: TerraformDevice, city: City)

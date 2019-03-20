package joguin.alien.terraformdevice

import joguin.alien.Power

/** Changes the Earth's environment so that it becomes inhabitable by the aliens.
  * Unfortunately it turns inhospitable for the native life forms.
  * Each device has its own defense power to protect itself */
final case class TerraformDevice(defensePower: Power)

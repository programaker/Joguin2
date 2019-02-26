package joguin.alien

import java.util.concurrent.ThreadLocalRandom

import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import joguin.alien.Invasion.InProgress
import joguin.earth.city.City

/** Attacks a city installing a Terraform Device in it, resulting an invasion.
  * The Terraform Devices gain a random defense power to make things more interesting */
object AlienArmy {
  def attack(city: City): Invasion = Invasion(
    TerraformDevice(fillPower(1000, 20000)),
    city,
    InProgress
  )

  //TODO => find some pure FP random generator
  private def fillPower(minPower: DefensePower, maxPower: DefensePower): DefensePower =
    refineV[Positive](ThreadLocalRandom.current.nextInt(minPower, maxPower + 1)).getOrElse(minPower)
}

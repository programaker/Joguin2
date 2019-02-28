package joguin.alien

import cats.InjectK
import cats.free.Free
import cats.free.Free._
import joguin.earth.city.City
import joguin.alien.Invasion.InProgress
import eu.timepit.refined.auto._

trait AlienArmyOp[A]
case class Attack(city: City) extends AlienArmyOp[Invasion]

class AlienArmy[F[_]](implicit I: InjectK[AlienArmyOp,F]) {
  def attack(city: City): Free[F,Invasion] = inject[AlienArmyOp,F](Attack(city))
}

object AlienArmy {
  implicit def create[F[_]](implicit I: InjectK[AlienArmyOp,F]): AlienArmy[F] = new AlienArmy[F]

  /** Attacks a city installing a Terraform Device in it, resulting an invasion.
    * The Terraform Devices gain a random defense power to make things more interesting */
  def attack[F[_]](city: City)(P: PowerGenerator[F]): Free[F,Invasion] = {
    import P._

    generatePower(min = 1000, max = 20000)
      .map(TerraformDevice)
      .map(Invasion(_, city, InProgress))
  }
}
package joguin.alien.terraformdevice

import cats.InjectK
import cats.free.Free
import cats.free.Free.inject
import joguin.alien.Power

sealed trait PowerGeneratorF[A]
final case class GeneratePower(min: Power, max: Power) extends PowerGeneratorF[Power]

final class PowerGeneratorOps[G[_]](implicit i: InjectK[PowerGeneratorF, G]) {
  def generatePower(min: Power, max: Power): Free[G, Power] =
    inject[PowerGeneratorF, G](GeneratePower(min, max))
}
object PowerGeneratorOps {
  implicit def create[G[_]](implicit i: InjectK[PowerGeneratorF, G]): PowerGeneratorOps[G] =
    new PowerGeneratorOps[G]
}

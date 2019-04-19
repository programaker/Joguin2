package joguin.alien.terraformdevice

import cats.InjectK
import cats.free.Free
import cats.free.Free.inject
import joguin.alien.Power

sealed trait PowerGeneratorF[A]
final case class GeneratePower(min: Power, max: Power) extends PowerGeneratorF[Power]

final class PowerGeneratorOps[C[_]](implicit i: InjectK[PowerGeneratorF, C]) {
  def generatePower(min: Power, max: Power): Free[C, Power] =
    inject[PowerGeneratorF, C](GeneratePower(min, max))
}
object PowerGeneratorOps {
  implicit def create[C[_]](implicit i: InjectK[PowerGeneratorF, C]): PowerGeneratorOps[C] =
    new PowerGeneratorOps[C]
}

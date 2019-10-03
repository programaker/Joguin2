package joguin.alien.terraformdevice

import cats.InjectK
import cats.free.Free
import cats.free.Free.inject
import joguin.alien.Power

sealed abstract class PowerGeneratorF[A] extends Product with Serializable
final case class GeneratePower(min: Power, max: Power) extends PowerGeneratorF[Power]

final class PowerGeneratorOps[C[_]](implicit i: InjectK[PowerGeneratorF, C]) {
  def generatePower(min: Power, max: Power): Free[C, Power] =
    inject(GeneratePower(min, max))
}

object PowerGeneratorOps {
  implicit def powerGeneratorOps[C[_]](implicit i: InjectK[PowerGeneratorF, C]): PowerGeneratorOps[C] =
    new PowerGeneratorOps[C]
}

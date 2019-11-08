package joguin.alien.terraformdevice

import cats.InjectK
import cats.free.Free
import cats.free.Free.inject
import joguin.alien.Power
import joguin.alien.terraformdevice.PowerGeneratorF.GeneratePower

sealed abstract class PowerGeneratorF[A] extends Product with Serializable

object PowerGeneratorF {
  final case class GeneratePower(min: Power, max: Power) extends PowerGeneratorF[Power]
}

final class PowerGeneratorOps[F[_]](implicit i: InjectK[PowerGeneratorF, F]) {
  def generatePower(min: Power, max: Power): Free[F, Power] =
    inject(GeneratePower(min, max))
}

object PowerGeneratorOps {
  implicit def powerGeneratorOps[F[_]](implicit i: InjectK[PowerGeneratorF, F]): PowerGeneratorOps[F] =
    new PowerGeneratorOps[F]
}

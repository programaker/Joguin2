package joguin.alien.terraformdevice

import cats.InjectK
import cats.free.Free
import cats.free.Free.inject
import joguin.alien.Power

sealed trait PowerGeneratorOp[A]
case class GeneratePower(min: Power, max: Power) extends PowerGeneratorOp[Power]

class PowerGenerator[F[_]](implicit I: InjectK[PowerGeneratorOp,F]) {
  def generatePower(min: Power, max: Power): Free[F,Power] =
    inject[PowerGeneratorOp,F](GeneratePower(min, max))
}

object PowerGenerator {
  implicit def create[F[_]](implicit I: InjectK[PowerGeneratorOp,F]): PowerGenerator[F] =
    new PowerGenerator[F]
}
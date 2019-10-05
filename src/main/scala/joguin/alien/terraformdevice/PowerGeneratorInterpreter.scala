package joguin.alien.terraformdevice

import java.util.concurrent.ThreadLocalRandom

import cats.~>
import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.Lazy
import joguin.alien.Power
import joguin.alien.PowerR
import joguin.alien.terraformdevice.PowerGeneratorF.GeneratePower

/** PowerGeneratorF root interpreter to any F that produces random numbers between min and max */
final class PowerGeneratorInterpreter[F[_]: Lazy] extends (PowerGeneratorF ~> F) {
  override def apply[A](fa: PowerGeneratorF[A]): F[A] = fa match {
    case GeneratePower(min, max) => randomPowerBetween(min, max)
  }

  private def randomPowerBetween(min: Power, max: Power): F[Power] =
    Lazy[F].lift(impureRandomPower(min, max))

  private def impureRandomPower(min: Power, max: Power): Power =
    refineV[PowerR](ThreadLocalRandom.current.nextInt(min, max + 1)) match {
      case Right(generatedPower) => generatedPower
      case Left(_)               => min //in case of error, falls back to min value and life goes on...
    }
}

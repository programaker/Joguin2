package joguin.alien.terraformdevice

import java.util.concurrent.ThreadLocalRandom

import cats.effect.IO
import cats.implicits._
import cats.~>
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.refineV
import joguin.alien.Power

/** PowerGeneratorOp interpreter for IO that produces a random numbers between min and max */
object IORandomPowerGenerator extends (PowerGeneratorOp ~> IO) {
  override def apply[A](op: PowerGeneratorOp[A]): IO[A] = op match {
    case GeneratePower(min, max) => randomPowerBetween(min, max)
  }

  private def randomPowerBetween(min: Power, max: Power): IO[Power] =
    IO(ThreadLocalRandom.current.nextInt(min, max + 1))
      .map(refineV[Positive](_))
      .map(_.leftMap(validationErrorToException))
      .flatMap(IO.fromEither)

  private def validationErrorToException(errorMessage: String): Exception =
    new Exception(errorMessage)
}
package joguin.alien.terraformdevice

import java.util.concurrent.ThreadLocalRandom

import cats.effect.IO
import cats.implicits._
import cats.~>
import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.alien.{Power, PowerR}

/** PowerGeneratorF root interpreter to IO that produces random numbers between min and max */
object PowerGeneratorIORandom extends (PowerGeneratorF ~> IO) {
  override def apply[A](fa: PowerGeneratorF[A]): IO[A] = fa match {
    case GeneratePower(min, max) => randomPowerBetween(min, max)
  }

  private def randomPowerBetween(min: Power, max: Power): IO[Power] =
    IO(ThreadLocalRandom.current.nextInt(min, max + 1))
      .map(refineV[PowerR](_))
      .map(_.leftMap(validationErrorToException))
      .flatMap(IO.fromEither)

  private def validationErrorToException(errorMessage: String): Exception =
    new Exception(errorMessage)
}

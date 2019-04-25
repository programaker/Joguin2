package joguin.alien.terraformdevice

import java.util.concurrent.ThreadLocalRandom

import cats.effect.IO
import cats.~>
import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.alien.{Power, PowerR}

/** PowerGeneratorF root interpreter to IO that produces random numbers between min and max */
object PowerGeneratorIOInterpreter extends (PowerGeneratorF ~> IO) {
  override def apply[A](fa: PowerGeneratorF[A]): IO[A] = fa match {
    case GeneratePower(min, max) => randomPowerBetween(min, max)
  }

  private def randomPowerBetween(min: Power, max: Power): IO[Power] =
    IO(impureRandomPower(min, max))
      .map {
        case Right(generatedPower) => generatedPower
        case Left(_) => min //in case of error, falls back to min value and life goes on...
      }

  private def impureRandomPower(min: Power, max: Power): Either[String, Power] =
    refineV[PowerR](ThreadLocalRandom.current.nextInt(min, max + 1))
}

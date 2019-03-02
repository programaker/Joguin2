package joguin.alien.terraformdevice

import java.util.concurrent.ThreadLocalRandom

import cats.effect.IO
import cats.~>
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.refineV
import eu.timepit.refined.auto._

object RandomPowerGenerator extends (PowerGeneratorOp ~> IO) {
  override def apply[A](op: PowerGeneratorOp[A]): IO[A] = op match {
    case GeneratePower(min, max) => IO {
      refineV[Positive](ThreadLocalRandom.current.nextInt(min, max + 1)).getOrElse(min)
    }
  }
}

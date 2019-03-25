package joguin.playerinteraction.wait

import cats.effect.IO
import cats.~>

import scala.concurrent.duration.FiniteDuration

object IOThreadSleepWait extends (WaitF ~> IO) {
  override def apply[A](fa: WaitF[A]): IO[A] = fa match {
    case Wait(duration) => sleep(duration)
  }

  private def sleep(duration: FiniteDuration): IO[Unit] =
    IO(Thread.sleep(duration.toMillis)).handleErrorWith(_ => IO.unit)
}

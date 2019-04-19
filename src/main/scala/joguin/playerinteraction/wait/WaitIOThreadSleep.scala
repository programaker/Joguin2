package joguin.playerinteraction.wait

import cats.effect.IO
import cats.~>

import scala.concurrent.duration.FiniteDuration

/** WaitF root interpreter to IO that uses Thread.sleep for waiting */
object WaitIOThreadSleep extends (WaitF ~> IO) {
  override def apply[A](fa: WaitF[A]): IO[A] = fa match {
    case WaitFor(duration) => sleep(duration)
  }

  private def sleep(duration: FiniteDuration): IO[Unit] =
    IO(Thread.sleep(duration.toMillis)).handleErrorWith(_ => IO.unit)
}

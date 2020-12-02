package joguin.playerinteraction.wait

import cats.syntax.flatMap._
import cats.syntax.applicativeError._
import cats.~>
import joguin.Lazy
import joguin.Recovery
import joguin.playerinteraction.wait.WaitF.WaitFor

import scala.concurrent.duration.FiniteDuration

/** WaitF root interpreter to any F that uses Thread.sleep for waiting */
final class WaitInterpreter[F[_]: Recovery: Lazy] extends (WaitF ~> F) {
  override def apply[A](fa: WaitF[A]): F[A] = fa match {
    case WaitFor(duration) => sleep(duration)
  }

  private def sleep(duration: FiniteDuration): F[Unit] =
    Recovery[F]
      .pure(duration)
      .flatMap(d => Lazy[F].lift(Thread.sleep(d.toMillis)))
      .handleError(_ => ())
}

package joguin.playerinteraction.wait

import cats.InjectK
import cats.free.Free
import cats.free.Free._
import joguin.playerinteraction.wait.WaitF.WaitFor

import scala.concurrent.duration.FiniteDuration

sealed trait WaitF[A]

object WaitF {
  final case class WaitFor(duration: FiniteDuration) extends WaitF[Unit]
}

final class WaitOps[F[_]](implicit i: InjectK[WaitF, F]) {
  def waitFor(duration: FiniteDuration): Free[F, Unit] =
    inject(WaitFor(duration))
}

object WaitOps {
  implicit def waitOps[F[_]](implicit i: InjectK[WaitF, F]): WaitOps[F] = new WaitOps[F]
}

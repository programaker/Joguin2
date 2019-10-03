package joguin.playerinteraction.wait

import cats.InjectK
import cats.free.Free
import cats.free.Free._

import scala.concurrent.duration.FiniteDuration

sealed abstract class WaitF[A] extends Product with Serializable
final case class WaitFor(duration: FiniteDuration) extends WaitF[Unit]

final class WaitOps[C[_]](implicit i: InjectK[WaitF, C]) {
  def waitFor(duration: FiniteDuration): Free[C, Unit] =
    inject(WaitFor(duration))
}

object WaitOps {
  implicit def waitOps[C[_]](implicit i: InjectK[WaitF, C]): WaitOps[C] = new WaitOps[C]
}

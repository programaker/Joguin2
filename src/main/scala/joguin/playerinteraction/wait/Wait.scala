package joguin.playerinteraction.wait

import cats.InjectK
import cats.free.Free
import cats.free.Free._

import scala.concurrent.duration.FiniteDuration

sealed trait WaitF[A]
final case class WaitFor(duration: FiniteDuration) extends WaitF[Unit]

final class WaitOps[G[_]](implicit i: InjectK[WaitF, G]) {
  def waitFor(duration: FiniteDuration): Free[G, Unit] =
    inject[WaitF, G](WaitFor(duration))
}
object WaitOps {
  implicit def create[G[_]](implicit i: InjectK[WaitF, G]): WaitOps[G] = new WaitOps[G]
}

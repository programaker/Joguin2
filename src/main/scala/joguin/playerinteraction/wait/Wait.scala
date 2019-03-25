package joguin.playerinteraction.wait

import cats.InjectK
import cats.free.Free
import cats.free.Free._

import scala.concurrent.duration.FiniteDuration

sealed trait WaitF[A]
final case class Wait(duration: FiniteDuration) extends WaitF[Unit]

final class WaitOps[G[_]](implicit I: InjectK[WaitF, G]) {
  def wait(duration: FiniteDuration): Free[G, Unit] =
    inject[WaitF, G](Wait(duration))
}
object WaitOps {
  implicit def create[G[_]](implicit I: InjectK[WaitF, G]): WaitOps[G] = new WaitOps[G]
}

package joguin.playerinteraction.interaction

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed abstract class InteractionF[A] extends Product with Serializable
final case class WriteMessage(message: String) extends InteractionF[Unit]
case object ReadAnswer extends InteractionF[String]

final class InteractionOps[C[_]](implicit i: InjectK[InteractionF, C]) {
  def writeMessage(message: String): Free[C, Unit] =
    inject(WriteMessage(message))

  def readAnswer: Free[C, String] =
    inject(ReadAnswer)
}

object InteractionOps {
  implicit def interactionOps[C[_]](implicit i: InjectK[InteractionF, C]): InteractionOps[C] =
    new InteractionOps[C]
}

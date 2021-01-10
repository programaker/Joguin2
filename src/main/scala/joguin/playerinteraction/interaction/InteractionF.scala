package joguin.playerinteraction.interaction

import cats.InjectK
import cats.free.Free
import cats.free.Free._
import joguin.playerinteraction.interaction.InteractionF.ReadAnswer
import joguin.playerinteraction.interaction.InteractionF.WriteMessage

sealed trait InteractionF[A]

object InteractionF {
  final case class WriteMessage(message: String) extends InteractionF[Unit]
  case object ReadAnswer extends InteractionF[String]
}

final class InteractionOps[F[_]](implicit i: InjectK[InteractionF, F]) {
  def writeMessage(message: String): Free[F, Unit] =
    inject(WriteMessage(message))

  def readAnswer: Free[F, String] =
    inject(ReadAnswer)
}

object InteractionOps {
  implicit def interactionOps[F[_]](implicit i: InjectK[InteractionF, F]): InteractionOps[F] =
    new InteractionOps[F]
}

package joguin.testutil.interpreter

import cats.Id
import cats.~>
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.interaction.ReadAnswer
import joguin.playerinteraction.interaction.WriteMessage

object InteractionIdInterpreter extends (InteractionF ~> Id) {
  override def apply[A](fa: InteractionF[A]): Id[A] = fa match {
    case WriteMessage(message) => ???
    case ReadAnswer => ???
  }
}

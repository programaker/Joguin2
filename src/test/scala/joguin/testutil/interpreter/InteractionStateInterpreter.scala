package joguin.testutil.interpreter

import cats.~>
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.interaction.ReadAnswer
import joguin.playerinteraction.interaction.WriteMessage
import joguin.testutil.interpreter.MessageAnswer._

/** InteractionF interpreter for State. For test purposes only */
final class InteractionStateInterpreter extends (InteractionF ~> TrackState) {
  override def apply[A](fa: InteractionF[A]): TrackState[A] = fa match {
    case WriteMessage(message) => writeMessage(message)
    case ReadAnswer => readAnswer
  }

  private def writeMessage(message: String): TrackState[Unit] = {
    ???
  }

  private def readAnswer: TrackState[String] = {
    ???
  }
}

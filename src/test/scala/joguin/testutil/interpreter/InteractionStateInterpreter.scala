package joguin.testutil.interpreter

import cats.~>
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.interaction.ReadAnswer
import joguin.playerinteraction.interaction.WriteMessage
import joguin.testutil.interpreter.WriteMessageTrack._

/** InteractionF interpreter for State. For test purposes only */
final class InteractionStateInterpreter[T](
  answers: IndexedTrack,
  state: IndexedTrackState[T]
) extends (InteractionF ~> IndexedTrackState) {

  override def apply[A](fa: InteractionF[A]): IndexedTrackState[A] = fa match {
    case WriteMessage(message) => writeMessage(message)
    case ReadAnswer => readAnswer
  }

  private def writeMessage(message: String): IndexedTrackState[Unit] = {
    ???
  }

  private def readAnswer: IndexedTrackState[String] = {
    ???
  }
}

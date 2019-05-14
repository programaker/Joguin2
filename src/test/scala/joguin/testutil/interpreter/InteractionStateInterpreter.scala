package joguin.testutil.interpreter

import cats.data.State
import cats.~>
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.interaction.ReadAnswer
import joguin.playerinteraction.interaction.WriteMessage
import joguin.testutil.interpreter.WriteMessageTrack._

/** InteractionF interpreter for State. For test purposes only */
final class InteractionStateInterpreter[T](answers: Map[Int, String]) extends (InteractionF ~> IndexedTrackState) {
  override def apply[A](fa: InteractionF[A]): IndexedTrackState[A] = fa match {
    case WriteMessage(message) => writeMessage(message)
    case ReadAnswer => readAnswer
  }

  private def writeMessage(message: String): IndexedTrackState[Unit] =
    State { track =>
      val newMap = track.indexedMessages + (track.lastIndex -> message)
      val newIndex = track.lastIndex + 1
      (WriteMessageTrack(newIndex, newMap), ())
    }

  private def readAnswer: IndexedTrackState[String] =
    State { track =>
      (track, answers.getOrElse(track.lastIndex, "<<fail>>"))
    }
}

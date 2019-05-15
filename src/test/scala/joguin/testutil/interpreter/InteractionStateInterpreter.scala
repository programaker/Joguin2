package joguin.testutil.interpreter

import cats.data.State
import cats.~>
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.interaction.ReadAnswer
import joguin.playerinteraction.interaction.WriteMessage
import joguin.testutil.interpreter.WriteMessageTrack._

/** InteractionF interpreter for State. For test purposes only */
final class InteractionStateInterpreter(val answers: Map[Int, String]) extends (InteractionF ~> MessageTrackState) {
  override def apply[A](fa: InteractionF[A]): MessageTrackState[A] = fa match {
    case WriteMessage(message) => writeMessage(message)
    case ReadAnswer => readAnswer
  }

  private def writeMessage(message: String): MessageTrackState[Unit] =
    State { track =>
      val newMap = track.indexedMessages + (track.lastIndex -> message)
      val newIndex = track.lastIndex + 1
      (WriteMessageTrack(newIndex, newMap), ())
    }

  private def readAnswer: MessageTrackState[String] =
    State { track =>
      val index = track.lastIndex
      val answer = answers.getOrElse(index, "<<fail>>")
      (track, answer)
    }
}

object InteractionStateInterpreter {
  def apply(answers: Map[Int, String]): InteractionStateInterpreter =
    new InteractionStateInterpreter(answers)
}

package joguin.testutil.interpreter

import cats.data.State
import cats.~>
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.interaction.ReadAnswer
import joguin.playerinteraction.interaction.WriteMessage
import joguin.testutil.interpreter.WriteMessageTrack._

/** InteractionF interpreter for State. For test purposes only */
final class InteractionStateInterpreter(val answers: Map[String, String]) extends (InteractionF ~> MessageTrackState) {
  override def apply[A](fa: InteractionF[A]): MessageTrackState[A] = fa match {
    case WriteMessage(message) => writeMessage(message)
    case ReadAnswer => readAnswer
  }

  private def writeMessage(message: String): MessageTrackState[Unit] =
    State { track =>
      val newMap = track.indexedMessages + (track.lastIndex -> message)
      val newMessage = message
      val newIndex = track.lastIndex + 1
      (WriteMessageTrack(newMessage, newIndex, newMap), ())
    }

  private def readAnswer: MessageTrackState[String] =
    State { track =>
      val question = track.lastMessage
      val answer = answers.getOrElse(question, "<<fail>>")
      (track, answer)
    }
}

object InteractionStateInterpreter {
  def apply(answers: Map[String, String]): InteractionStateInterpreter =
    new InteractionStateInterpreter(answers)
}

package joguin.testutil.interpreter

import cats.data.State
import cats.~>
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.interaction.ReadAnswer
import joguin.playerinteraction.interaction.WriteMessage

/** InteractionF interpreter for State. For test purposes only */
final class InteractionStateInterpreter extends (InteractionF ~> MessageTrackState) {
  override def apply[A](fa: InteractionF[A]): MessageTrackState[A] = fa match {
    case WriteMessage(message) => writeMessage(message)
    case ReadAnswer            => readAnswer
  }

  private def writeMessage(message: String): MessageTrackState[Unit] =
    State { track =>
      val newMessage = message
      val newMap = track.indexedMessages.updated(track.currentIndex, message)
      val newIndex = track.currentIndex + 1
      val newTrack = track.copy(currentMessage = newMessage, currentIndex = newIndex, indexedMessages = newMap)
      (newTrack, ())
    }

  private def readAnswer: MessageTrackState[String] =
    State { track =>
      val question = track.currentMessage
      val answers = track.answersByQuestion.getOrElse(question, Nil)

      val (currentAnswer, nextAnswers) = answers match {
        case head :: tail => (head, tail)
        case Nil          => ("<<fail>>", Nil)
      }

      val newAnswers = track.answersByQuestion.updated(question, nextAnswers)
      val newTrack = track.copy(answersByQuestion = newAnswers)

      (newTrack, currentAnswer)
    }
}

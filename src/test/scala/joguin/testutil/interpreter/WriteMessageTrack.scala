package joguin.testutil.interpreter

import cats.data.State
import joguin.Lazy

final case class WriteMessageTrack(
  currentMessage: String,
  currentIndex: Int,
  indexedMessages: Map[Int, String],
  answersByQuestion: Map[String, List[String]]
)

object WriteMessageTrack {
  type MessageTrackState[A] = State[WriteMessageTrack, A]

  def build(answersByQuestion: Map[String, List[String]]): WriteMessageTrack =
    new WriteMessageTrack("", 0, Map.empty, answersByQuestion)

  implicit val stateLazyEff: Lazy[MessageTrackState] = new Lazy[MessageTrackState] {
    override def lift[A](a: => A): MessageTrackState[A] = State.pure(a)
  }
}

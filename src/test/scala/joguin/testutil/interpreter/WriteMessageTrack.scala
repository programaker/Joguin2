package joguin.testutil.interpreter

import cats.data.State
import joguin.LazyEff

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

  implicit val stateLazyEff:LazyEff[MessageTrackState] = new LazyEff[MessageTrackState] {
    override def wrap[A](a: =>A): MessageTrackState[A] = State.pure(a)
  }
}

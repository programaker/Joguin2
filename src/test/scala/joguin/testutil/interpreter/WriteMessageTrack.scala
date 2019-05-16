package joguin.testutil.interpreter

import cats.data.State
import joguin.LazyEff

final case class WriteMessageTrack(
  lastMessage: String,
  lastIndex: Int,
  indexedMessages: Map[Int, String]
)

object WriteMessageTrack {
  type MessageTrackState[A] = State[WriteMessageTrack, A]

  def empty: WriteMessageTrack =
    new WriteMessageTrack("", 0, Map.empty)

  implicit val stateLazyEff: LazyEff[MessageTrackState] = new LazyEff[MessageTrackState] {
    override def wrap[A](a: =>A): MessageTrackState[A] = State.pure(a)
  }
}

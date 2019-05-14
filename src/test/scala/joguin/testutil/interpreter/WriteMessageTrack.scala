package joguin.testutil.interpreter

import cats.data.State

final case class WriteMessageTrack(lastIndex: Int, indexedMessages: Map[Int, String])

object WriteMessageTrack {
  type IndexedTrackState[A] = State[WriteMessageTrack, A]
}

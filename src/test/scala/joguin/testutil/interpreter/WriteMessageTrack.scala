package joguin.testutil.interpreter

import cats.data.State

final case class WriteMessageTrack(index: Int, message: String)

object WriteMessageTrack {
  type IndexedTrack = Map[Int, WriteMessageTrack]
  type IndexedTrackState[A] = State[IndexedTrack, A]
}

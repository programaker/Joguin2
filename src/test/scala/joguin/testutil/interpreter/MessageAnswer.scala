package joguin.testutil.interpreter

import cats.data.State

final case class MessageAnswer(index: Int, message: String)

object MessageAnswer {
  type Track = Map[Int, MessageAnswer]
  type TrackState[A] = State[Track, A]
}

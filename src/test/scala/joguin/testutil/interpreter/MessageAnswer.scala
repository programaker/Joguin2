package joguin.testutil.interpreter

import cats.data.State

final case class MessageAnswer(message: String, answer: String)

object MessageAnswer {
  type Track = Map[Int, MessageAnswer]
  type TrackState[A] = State[Track, A]
}

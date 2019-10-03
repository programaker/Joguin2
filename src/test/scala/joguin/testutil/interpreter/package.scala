package joguin.testutil

import cats.data.State
import joguin.Lazy

package object interpreter {
  type MessageTrackState[A] = State[WriteMessageTrack, A]

  implicit val StateLazy: Lazy[MessageTrackState] = new Lazy[MessageTrackState] {
    override def lift[A](a: => A): MessageTrackState[A] = State.pure(a)
  }
}

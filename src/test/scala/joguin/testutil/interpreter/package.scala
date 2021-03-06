package joguin.testutil

import cats.data.State
import joguin.Lazy
import joguin.alien.terraformdevice.PowerGeneratorInterpreter
import joguin.earth.city.CityRepositoryInterpreter
import joguin.playerinteraction.message.MessagesInterpreter

package object interpreter {
  type MessageTrackState[A] = State[WriteMessageTrack, A]

  implicit val StateLazy: Lazy[MessageTrackState] = new Lazy[MessageTrackState] {
    override def lift[A](a: => A): MessageTrackState[A] = State.pure(a)
  }

  val MessagesInterpreter: MessagesInterpreter[MessageTrackState] =
    new MessagesInterpreter[MessageTrackState]

  val CityRepositoryInterpreter: CityRepositoryInterpreter[MessageTrackState] =
    new CityRepositoryInterpreter[MessageTrackState]

  val PowerGeneratorInterpreter: PowerGeneratorInterpreter[MessageTrackState] =
    new PowerGeneratorInterpreter[MessageTrackState]

  val InteractionInterpreter: InteractionStateTestInterpreter =
    new InteractionStateTestInterpreter

  val WaitInterpreter: WaitTestInterpreter[MessageTrackState] =
    new WaitTestInterpreter[MessageTrackState]
}

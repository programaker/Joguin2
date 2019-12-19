package joguin.testutil

import cats.data.EitherK
import cats.data.State
import cats.~>
import joguin.Lazy
import joguin.alien.terraformdevice.PowerGeneratorInterpreter
import joguin.earth.city.CityRepositoryInterpreter
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.MessagesF
import joguin.playerinteraction.message.MessagesInterpreter
import joguin.playerinteraction.wait.WaitF

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

  /* Coproduct algebra for messageInteractionWaitInterpreter */
  type F1[A] = EitherK[MessagesF, InteractionF, A]
  type MessageInteractionWaitF[A] = EitherK[WaitF, F1, A]

  /** MessageInteractionWaitF composite interpreter to State.
   * To test the game steps that need MessagesF, InteractionF and WaitF algebras
   * in isolation from the whole game */
  def messageInteractionWaitInterpreter: MessageInteractionWaitF ~> MessageTrackState = {
    val i1 = MessagesInterpreter or InteractionInterpreter
    val iMessageInteractionWait = WaitInterpreter or i1

    iMessageInteractionWait
  }
}

package joguin.testutil.interpreter

import cats.data.EitherK
import cats.~>
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.MessageSourceF
import joguin.playerinteraction.message.MessageSourceInterpreter
import joguin.playerinteraction.message.MessagesF
import joguin.playerinteraction.message.MessagesInterpreter
import joguin.playerinteraction.wait.WaitF
import joguin.testutil.interpreter.WriteMessageTrack.MessageTrackState

/** ExploreStepF composite interpreter to State. To test the game step in isolation from the whole game */
object ExploreStepInterpreter {
  type F1[A] = EitherK[MessageSourceF, MessagesF, A]
  type F2[A] = EitherK[InteractionF, F1, A]
  type ExploreStepF[A] = EitherK[WaitF, F2, A]

  def build: ExploreStepF ~> MessageTrackState = {
    val i1 = messageSourceInterpreter or messagesInterpreter
    val i2 = InteractionStateInterpreter() or i1
    waitInterpreter or i2
  }

  private val messageSourceInterpreter: MessageSourceInterpreter[MessageTrackState] =
    MessageSourceInterpreter[MessageTrackState]

  private val messagesInterpreter: MessagesInterpreter[MessageTrackState] =
    MessagesInterpreter[MessageTrackState]

  private val waitInterpreter: WaitInterpreter[MessageTrackState] =
    WaitInterpreter[MessageTrackState]
}

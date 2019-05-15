package joguin.testutil.interpreter

import cats.data.EitherK
import cats.~>
import joguin.alien.terraformdevice.PowerGeneratorF
import joguin.alien.terraformdevice.PowerGeneratorInterpreter
import joguin.earth.city.CityRepositoryF
import joguin.earth.city.CityRepositoryInterpreter
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.MessageSourceF
import joguin.playerinteraction.message.MessageSourceInterpreter
import joguin.playerinteraction.message.MessagesF
import joguin.playerinteraction.message.MessagesInterpreter
import joguin.testutil.interpreter.WriteMessageTrack.MessageTrackState

object CreateCharacterStepInterpreter {
  type F1[A] = EitherK[MessageSourceF, MessagesF, A]
  type F2[A] = EitherK[InteractionF, F1, A]
  type F3[A] = EitherK[CityRepositoryF, F2, A]
  type CreateCharacterStepF[A] = EitherK[PowerGeneratorF, F3, A]

  def build(answers: Map[Int, String]): CreateCharacterStepF ~> MessageTrackState = {
    val i1 = messageSourceInterpreter or messagesInterpreter
    val i2 = InteractionStateInterpreter(answers) or i1
    val i3 = cityRepositoryInterpreter or i2
    powerGeneratorInterpreter or i3
  }

  private val messageSourceInterpreter: MessageSourceInterpreter[MessageTrackState] =
    MessageSourceInterpreter[MessageTrackState]

  private val messagesInterpreter: MessagesInterpreter[MessageTrackState] =
    MessagesInterpreter[MessageTrackState]

  private val cityRepositoryInterpreter: CityRepositoryInterpreter[MessageTrackState] =
    CityRepositoryInterpreter[MessageTrackState]

  private val powerGeneratorInterpreter: PowerGeneratorInterpreter[MessageTrackState] =
    PowerGeneratorInterpreter[MessageTrackState]
}

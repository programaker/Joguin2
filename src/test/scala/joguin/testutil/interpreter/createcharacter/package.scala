package joguin.testutil.interpreter

import cats.data.EitherK
import cats.~>
import joguin.alien.terraformdevice.PowerGeneratorF
import joguin.earth.city.CityRepositoryF
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.MessageSourceF
import joguin.playerinteraction.message.MessagesF

package object createcharacter {
  type F1[A] = EitherK[MessageSourceF, MessagesF, A]
  type F2[A] = EitherK[InteractionF, F1, A]
  type F3[A] = EitherK[CityRepositoryF, F2, A]
  type CreateCharacterStepF[A] = EitherK[PowerGeneratorF, F3, A]

  /** CreateCharacterStepF composite interpreter to State. To test the game step in isolation from the whole game */
  def createCharacterStepTestInterpreter(): CreateCharacterStepF ~> MessageTrackState = {
    val i1 = MessageSourceInterpreter or MessagesInterpreter
    val i2 = InteractionInterpreter or i1
    val i3 = CityRepositoryInterpreter or i2
    val iCreateCharacter = PowerGeneratorInterpreter or i3

    iCreateCharacter
  }
}

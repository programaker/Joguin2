package joguin.testutil.interpreter

import cats.data.EitherK
import cats.~>
import joguin.alien.terraformdevice.PowerGeneratorF
import joguin.earth.city.CityRepositoryF
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.MessagesF

package object createcharacter {
  type F1[A] = EitherK[MessagesF, InteractionF, A]
  type F2[A] = EitherK[CityRepositoryF, F1, A]
  type CreateCharacterStepF[A] = EitherK[PowerGeneratorF, F2, A]

  /** CreateCharacterStepF composite interpreter to State. To test the game step in isolation from the whole game */
  def createCharacterStepTestInterpreter(): CreateCharacterStepF ~> MessageTrackState = {
    val i1 = MessagesInterpreter or InteractionInterpreter
    val i2 = CityRepositoryInterpreter or i1
    val iCreateCharacter = PowerGeneratorInterpreter or i2

    iCreateCharacter
  }
}

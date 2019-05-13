package joguin.testutil.interpreter

import cats.Id
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

object CreateCharacterStepInterpreter {
  type F1[A] = EitherK[MessageSourceF, MessagesF, A]
  type F2[A] = EitherK[InteractionF, F1, A]
  type F3[A] = EitherK[CityRepositoryF, F2, A]
  type CreateCharacterStepF[A] = EitherK[PowerGeneratorF, F3, A]

  def build(
    messageSourceInterpreter: MessageSourceInterpreter[Id],
    cityRepositoryInterpreter: CityRepositoryInterpreter[Id],
    powerGeneratorIOInterpreter: PowerGeneratorInterpreter[Id]
  ): CreateCharacterStepF ~> Id = {


    ???
  }
}

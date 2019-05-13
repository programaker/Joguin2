package joguin.testutil.interpreter

import cats.Id
import joguin.alien.terraformdevice.PowerGeneratorInterpreter
import joguin.earth.city.CityRepositoryInterpreter
import joguin.playerinteraction.message.MessageSourceInterpreter

object Interpreters {
  val messageSourceInterpreter: MessageSourceInterpreter[Id] =
    MessageSourceInterpreter[Id]

  val cityRepositoryInterpreter: CityRepositoryInterpreter[Id] =
    CityRepositoryInterpreter[Id]

  val powerGeneratorInterpreter: PowerGeneratorInterpreter[Id] =
    PowerGeneratorInterpreter[Id]

  val waitInterpreter: WaitIdInterpreter.type = WaitIdInterpreter
}

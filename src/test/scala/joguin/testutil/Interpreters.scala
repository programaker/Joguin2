package joguin.testutil

import cats.Id
import joguin.alien.Power
import joguin.alien.terraformdevice.PowerGeneratorInterpreter

object Interpreters {
  val powerGeneratorInterpreter: PowerGeneratorInterpreter[Id] = PowerGeneratorInterpreter[Id](liftPower)
  private def liftPower(power: =>Power): Id[Power] = power
}

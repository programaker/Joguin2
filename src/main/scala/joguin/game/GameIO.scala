package joguin.game

import cats.effect.IO
import cats.~>
import joguin.alien.terraformdevice.PowerGeneratorIORandom
import joguin.earth.city.CityRepositoryIOHardcoded
import joguin.game.progress.GameProgressRepositoryF
import joguin.playerinteraction.interaction.InteractionIOConsole
import joguin.playerinteraction.message.{MessageSourceIOHardcoded, MessagesIOResourceBundle}
import joguin.playerinteraction.wait.WaitIOThreadSleep

/** GameF composite interpreter to IO */
object GameIO {
  def composite(gameProgressRepository: GameProgressRepositoryF ~> IO): GameF ~> IO = {
    val f1 = MessagesIOResourceBundle or MessageSourceIOHardcoded
    val f2 = f1 or InteractionIOConsole
    val f3 = f2 or CityRepositoryIOHardcoded
    val f4 = f3 or gameProgressRepository
    val f5 = f4 or PowerGeneratorIORandom
    f5 or WaitIOThreadSleep

    /*MessagesIOResourceBundle or
      MessageSourceIOHardcoded or
      InteractionIOConsole or
      CityRepositoryIOHardcoded or
      gameProgressRepository or
      PowerGeneratorIORandom or
      WaitIOThreadSleep*/
  }
}

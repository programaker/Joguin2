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
    val f2 = InteractionIOConsole or f1
    val f3 = CityRepositoryIOHardcoded or f2
    val f4 = gameProgressRepository or f3
    val f5 = PowerGeneratorIORandom or f4
    WaitIOThreadSleep or f5

    /*MessagesIOResourceBundle or
      MessageSourceIOHardcoded or
      InteractionIOConsole or
      CityRepositoryIOHardcoded or
      gameProgressRepository or
      PowerGeneratorIORandom or
      WaitIOThreadSleep*/
  }
}

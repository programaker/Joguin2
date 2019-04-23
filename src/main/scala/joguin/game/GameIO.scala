package joguin.game

import java.io.File

import cats.effect.IO
import cats.~>
import joguin.alien.terraformdevice.PowerGeneratorIORandom
import joguin.earth.city.CityRepositoryIOHardcoded
import joguin.game.progress.GameProgressRepositoryIOFile
import joguin.playerinteraction.interaction.InteractionIOConsole
import joguin.playerinteraction.message.{MessageSourceIOHardcoded, MessagesIOResourceBundle}
import joguin.playerinteraction.wait.WaitIOThreadSleep

/** GameF composite interpreter to IO */
object GameIO {
  def interpreter: GameF ~> IO = {
    //The interpreter composition was written this way (with variables)
    //to match the Coproduct composition (see the game package object) and
    //make the order easier to see.
    //
    //This is important, as the interpreter composition must be
    //in the same order of the Coproduct composition and, without the
    //variables, it would be "upside-down" in relation to the Coproduct
    val i1 = MessagesIOResourceBundle or MessageSourceIOHardcoded
    val i2 = InteractionIOConsole or i1
    val i3 = CityRepositoryIOHardcoded or i2
    val i4 = gameProgressRepository or i3
    val i5 = PowerGeneratorIORandom or i4
    WaitIOThreadSleep or i5
  }

  private val gameProgressRepository =
    GameProgressRepositoryIOFile(new File("saved-game/last-progress.prog"))
}

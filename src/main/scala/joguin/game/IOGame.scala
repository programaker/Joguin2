package joguin.game

import java.io.File

import cats.effect.IO
import cats.~>
import joguin.alien.terraformdevice.IORandomPowerGenerator
import joguin.earth.city.IOHardcodedCityRepository
import joguin.game.progress.IOFileGameProgressRepository
import joguin.playerinteraction.interaction.IOConsoleInteraction
import joguin.playerinteraction.message.{IOHardcodedMessageSource, IOResourceBundleMessages}
import joguin.playerinteraction.wait.IOThreadSleepWait

/** GameF composite interpreter to IO */
object IOGame {
  def interpreter: GameF ~> IO = {
    //The interpreter composition was written this way (with variables)
    //to match the Coproduct composition (see the game package object) and
    //make the order easier to see.
    //
    //This is important, as the interpreter composition must be
    //in the same order of the Coproduct composition and, without the
    //variables, it would be "upside-down" in relation to the Coproduct
    val i1 = IOResourceBundleMessages or IOHardcodedMessageSource
    val i2 = IOConsoleInteraction or i1
    val i3 = IOHardcodedCityRepository or i2
    val i4 = gameProgressRepository or i3
    val i5 = IORandomPowerGenerator or i4
    IOThreadSleepWait or i5
  }

  private val gameProgressRepository =
    IOFileGameProgressRepository(new File("saved-game/last-progress.prog"))
}

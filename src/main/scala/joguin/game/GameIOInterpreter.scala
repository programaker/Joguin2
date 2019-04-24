package joguin.game

import java.io.File

import cats.effect.IO
import cats.~>
import joguin.alien.terraformdevice.PowerGeneratorIOInterpreter
import joguin.earth.city.CityRepositoryInterpreter
import joguin.game.progress.GameProgressRepositoryIOInterpreter
import joguin.playerinteraction.interaction.InteractionIOInterpreter
import joguin.playerinteraction.message.{MessageSourceIOInterpreter, MessagesIOInterpreter}
import joguin.playerinteraction.wait.WaitIOInterpreter

/** GameF composite interpreter to IO */
object GameIOInterpreter {
  def build: GameF ~> IO = {
    //The interpreter composition was written this way (with variables)
    //to match the Coproduct composition (see the game package object) and
    //make the order easier to see.
    //
    //This is important, as the interpreter composition must be
    //in the same order of the Coproduct composition and, without the
    //variables, it would be "upside-down" in relation to the Coproduct
    val i1 = MessagesIOInterpreter or MessageSourceIOInterpreter
    val i2 = InteractionIOInterpreter or i1
    val i3 = cityRepositoryInterpreter or i2
    val i4 = gameProgressRepositoryIOInterpreter or i3
    val i5 = PowerGeneratorIOInterpreter or i4
    WaitIOInterpreter or i5
  }

  private val gameProgressRepositoryIOInterpreter =
    GameProgressRepositoryIOInterpreter(new File("saved-game/last-progress.prog"))

  private val cityRepositoryInterpreter =
    CityRepositoryInterpreter[IO]
}

package joguin.game

import java.io.File

import cats.effect.IO
import cats.~>
import joguin.alien.terraformdevice.PowerGeneratorInterpreter
import joguin.earth.city.CityRepositoryInterpreter
import joguin.game.progress.GameProgressRepositoryIOInterpreter
import joguin.playerinteraction.interaction.InteractionIOInterpreter
import joguin.playerinteraction.message.MessageSourceInterpreter
import joguin.playerinteraction.message.MessagesInterpreter
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
    val i1 = messagesIOInterpreter or messageSourceIOInterpreter
    val i2 = InteractionIOInterpreter or i1
    val i3 = cityRepositoryIOInterpreter or i2
    val i4 = gameProgressRepositoryIOInterpreter or i3
    val i5 = powerGeneratorIOInterpreter or i4
    WaitIOInterpreter or i5
  }

  private val messageSourceIOInterpreter =
    MessageSourceInterpreter[IO]

  private val messagesIOInterpreter =
    new MessagesInterpreter[IO]

  private val cityRepositoryIOInterpreter =
    CityRepositoryInterpreter[IO]

  private val gameProgressRepositoryIOInterpreter =
    GameProgressRepositoryIOInterpreter(new File("saved-game/last-progress.prog"))

  private val powerGeneratorIOInterpreter =
    PowerGeneratorInterpreter[IO]
}

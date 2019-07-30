package joguin.game

import java.io.File

import cats.effect.IO
import cats.~>
import joguin.alien.terraformdevice.PowerGeneratorInterpreter
import joguin.earth.city.CityRepositoryInterpreter
import joguin.game.progress.GameProgressRepositoryInterpreter
import joguin.playerinteraction.interaction.InteractionInterpreter
import joguin.playerinteraction.message.MessageSourceInterpreter
import joguin.playerinteraction.message.MessagesInterpreter
import joguin.playerinteraction.wait.WaitInterpreter

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
    val i1 = messagesInterpreter or messageSourceInterpreter
    val i2 = interactionInterpreter or i1
    val i3 = cityRepositoryInterpreter or i2
    val i4 = gameProgressRepositoryInterpreter or i3
    val i5 = powerGeneratorInterpreter or i4
    waitInterpreter or i5
  }

  private val messageSourceInterpreter =
    MessageSourceInterpreter[IO]

  private val interactionInterpreter =
    InteractionInterpreter[IO]

  private val messagesInterpreter =
    MessagesInterpreter[IO]

  private val cityRepositoryInterpreter =
    CityRepositoryInterpreter[IO]

  private val gameProgressRepositoryInterpreter =
    GameProgressRepositoryInterpreter[IO](new File("saved-game/last-progress.prog"))

  private val powerGeneratorInterpreter =
    PowerGeneratorInterpreter[IO]

  private val waitInterpreter =
    WaitInterpreter[IO]
}

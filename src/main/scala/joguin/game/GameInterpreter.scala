package joguin.game

import java.io.File

import cats.~>
import joguin.Lazy
import joguin.Recovery
import joguin.alien.terraformdevice.PowerGeneratorInterpreter
import joguin.earth.city.CityRepositoryInterpreter
import joguin.game.progress.GameProgressRepositoryInterpreter
import joguin.playerinteraction.interaction.InteractionInterpreter
import joguin.playerinteraction.message.MessageSourceInterpreter
import joguin.playerinteraction.message.MessagesInterpreter
import joguin.playerinteraction.wait.WaitInterpreter

/** GameF composite interpreter to any F */
final class GameInterpreter[F[_]: Recovery: Lazy] {
  def build: GameF ~> F = {
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
    MessageSourceInterpreter[F]

  private val interactionInterpreter =
    InteractionInterpreter[F]

  private val messagesInterpreter =
    MessagesInterpreter[F]

  private val cityRepositoryInterpreter =
    CityRepositoryInterpreter[F]

  private val gameProgressRepositoryInterpreter =
    GameProgressRepositoryInterpreter[F](new File("saved-game/last-progress.prog"))

  private val powerGeneratorInterpreter =
    PowerGeneratorInterpreter[F]

  private val waitInterpreter =
    WaitInterpreter[F]
}

object GameInterpreter {
  def apply[F[_]: Recovery: Lazy]: GameInterpreter[F] = new GameInterpreter[F]
}

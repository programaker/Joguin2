package joguin.game.step.createcharacter

import cats.effect.IO
import cats.~>
import joguin.alien.terraformdevice.PowerGeneratorIORandom
import joguin.playerinteraction.MessagesAndInteractionIO

/** CreateCharacterF composite interpreter to IO */
object CreateCharacterIO {
  val composite: CreateCharacterF ~> IO =
    MessagesAndInteractionIO.composite or
    PowerGeneratorIORandom
}

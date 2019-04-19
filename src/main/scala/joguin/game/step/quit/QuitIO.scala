package joguin.game.step.quit

import cats.effect.IO
import cats.~>
import joguin.playerinteraction.MessagesAndInteractionIO

/** QuitF composite interpreter to IO */
object QuitIO {
  val composite: QuitF ~> IO =
    MessagesAndInteractionIO.composite
}

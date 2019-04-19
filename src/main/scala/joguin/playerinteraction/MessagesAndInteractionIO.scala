package joguin.playerinteraction

import cats.effect.IO
import cats.~>
import joguin.playerinteraction.interaction.InteractionIOConsole
import joguin.playerinteraction.message.MessagesAndSourceIO

/** MessagesAndInteractionF composite interpreter to IO */
object MessagesAndInteractionIO {
  val composite: MessagesAndInteractionF ~> IO =
    MessagesAndSourceIO.composite or
    InteractionIOConsole
}

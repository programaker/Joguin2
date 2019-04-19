package joguin.playerinteraction.message

import cats.effect.IO
import cats.~>

/** MessagesAndSourceF composite interpreter to IO */
object MessagesAndSourceIO {
  val composite: MessagesAndSourceF ~> IO =
    MessagesIOResourceBundle or
    MessageSourceIOHardcoded
}

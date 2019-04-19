package joguin.playerinteraction

import cats.data.EitherK

package object message {
  type MessagesAndSourceF[A] = EitherK[MessagesF, MessageSourceF, A]
}

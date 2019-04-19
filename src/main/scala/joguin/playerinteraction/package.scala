package joguin

import cats.data.EitherK
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.MessagesAndSourceF

package object playerinteraction {
  type MessagesAndInteractionF[A] = EitherK[MessagesAndSourceF, InteractionF, A]
}

package joguin.game.step

import cats.data.EitherK
import eu.timepit.refined.W
import eu.timepit.refined.string.MatchesRegex
import joguin.playerinteraction.interaction.InteractionOp
import joguin.playerinteraction.message.MessagesOp

package object quit {
  type QuitOp[A] = EitherK[InteractionOp,MessagesOp,A]
  type QuitAnswers = MatchesRegex[W.`"""^[yn]$"""`.T]
}

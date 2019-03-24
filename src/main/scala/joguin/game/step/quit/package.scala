package joguin.game.step

import cats.data.EitherK
import eu.timepit.refined.W
import eu.timepit.refined.string.MatchesRegex
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.{MessageSourceF, MessagesF}

package object quit {
  type F1[A] = EitherK[InteractionF, MessagesF, A]
  type QuitF[A] = EitherK[F1, MessageSourceF, A]

  type QuitAnswers = MatchesRegex[W.`"""^[yn]$"""`.T]
}

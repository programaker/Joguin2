package joguin.game.step

import cats.data.EitherK
import eu.timepit.refined.W
import eu.timepit.refined.string.MatchesRegex
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.{MessageSourceF, MessagesF}

package object quit {
  type QuitF[A] = EitherK[MessageSourceF, EitherK[MessagesF, InteractionF, ?], A]
  type YesNo = MatchesRegex[W.`"""^[yn]$"""`.T]
}

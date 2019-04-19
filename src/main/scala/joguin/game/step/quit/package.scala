package joguin.game.step

import eu.timepit.refined.W
import eu.timepit.refined.string.MatchesRegex
import joguin.playerinteraction.MessagesAndInteractionF

package object quit {
  type YesNo = MatchesRegex[W.`"""^[yn]$"""`.T]
  type QuitF[A] = MessagesAndInteractionF[A]
}

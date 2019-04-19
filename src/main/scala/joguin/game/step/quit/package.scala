package joguin.game.step

import eu.timepit.refined.W
import eu.timepit.refined.string.MatchesRegex
import joguin.playerinteraction.MessagesAndInteractionF

package object quit {
  type QuitOptionR = MatchesRegex[W.`"""^[yn]$"""`.T]
  type QuitF[A] = MessagesAndInteractionF[A]
}

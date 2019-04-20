package joguin.game.step

import eu.timepit.refined.W
import eu.timepit.refined.string.MatchesRegex

package object quit {
  type QuitOptionR = MatchesRegex[W.`"""^[yn]$"""`.T]
}

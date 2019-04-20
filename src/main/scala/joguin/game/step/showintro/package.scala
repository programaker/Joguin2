package joguin.game.step

import eu.timepit.refined.W
import eu.timepit.refined.string.MatchesRegex

package object showintro {
  type ShowIntroOptionR = MatchesRegex[W.`"""^[nqr]$"""`.T]
  type ShowIntroOptionNoRestoreR = MatchesRegex[W.`"""^[nq]$"""`.T]
}

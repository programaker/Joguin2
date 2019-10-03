package joguin.game.step

import eu.timepit.refined.W
import eu.timepit.refined.refineV
import eu.timepit.refined.string.MatchesRegex

package object quit {
  type QuitOptionR = MatchesRegex[W.`"""^[yn]$"""`.T]

  def parseQuitOption(s: String): Option[QuitOption] =
    refineV[QuitOptionR](s.toLowerCase).toOption.map(_.value match {
      case "y" => Yes
      case "n" => No
    })
}

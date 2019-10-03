package joguin.game.step

import eu.timepit.refined.W
import eu.timepit.refined.refineV
import eu.timepit.refined.string.MatchesRegex

package object showintro {
  type ShowIntroOptionR = MatchesRegex[W.`"""^[nqr]$"""`.T]
  type ShowIntroOptionNoRestoreR = MatchesRegex[W.`"""^[nq]$"""`.T]

  def parseShowIntroOption(s: String, hasSavedProgress: Boolean): Option[ShowIntroOption] = {
    val sanitizedS = s.toLowerCase()

    val refAnswer =
      if (hasSavedProgress) {
        refineV[ShowIntroOptionR](sanitizedS)
      } else {
        refineV[ShowIntroOptionNoRestoreR](sanitizedS)
      }

    refAnswer.toOption.map(_.value match {
      case "n" => NewGame
      case "q" => QuitGame
      case "r" => RestoreGame
    })
  }
}

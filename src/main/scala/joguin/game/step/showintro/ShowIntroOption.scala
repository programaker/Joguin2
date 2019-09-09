package joguin.game.step.showintro

import eu.timepit.refined.refineV

sealed abstract class ShowIntroOption extends Product with Serializable
case object NewGame extends ShowIntroOption
case object RestoreGame extends ShowIntroOption
case object QuitGame extends ShowIntroOption

object ShowIntroOption {
  def parse(s: String, hasSavedProgress: Boolean): Option[ShowIntroOption] = {
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

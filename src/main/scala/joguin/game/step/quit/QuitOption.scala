package joguin.game.step.quit

import eu.timepit.refined.refineV

trait QuitOption
case object Yes extends QuitOption
case object No extends QuitOption

object QuitOption {
  def parse(s: String): Option[QuitOption] =
    refineV[QuitOptionR](s.toLowerCase).toOption.map(_.value match {
      case "y" => Yes
      case "n" => No
    })
}

package joguin.game.step

import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.refineV
import joguin.game.step.fight.FightOption.FightAliens
import joguin.game.step.fight.FightOption.Retreat
import joguin.playerinteraction.message.LocalizedMessageSource
import joguin.playerinteraction.message.MessageSource.FightMessageSource

package object fight {
  type LocalizedFightMessageSource = LocalizedMessageSource[FightMessageSource.type]

  type FightOptionR = Equal["f"] Or Equal["r"] Or Equal["F"] Or Equal["R"]

  def parseFightOption(s: String): Option[FightOption] =
    refineV[FightOptionR](s).toOption
      .map(_.value match {
        case "f" | "F" => FightAliens
        case "r" | "R" => Retreat
      })
}

package joguin.game.step

import eu.timepit.refined.W
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.refineV
import joguin.game.step.fight.FightOption.FightAliens
import joguin.game.step.fight.FightOption.Retreat
import joguin.playerinteraction.message.LocalizedMessageSource
import joguin.playerinteraction.message.MessageSource.FightMessageSource

package object fight {
  type LocalizedFightMessageSource = LocalizedMessageSource[FightMessageSource.type]

  type FightOptionR = Equal[W.`"f"`.T] Or Equal[W.`"r"`.T]

  def parseFightOption(s: String): Option[FightOption] =
    refineV[FightOptionR](s.toLowerCase).toOption
      .map(_.value match {
        case "f" => FightAliens
        case "r" => Retreat
      })
}

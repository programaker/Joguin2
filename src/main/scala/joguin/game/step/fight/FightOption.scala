package joguin.game.step.fight

sealed trait FightOption

object FightOption {
  case object FightAliens extends FightOption
  case object Retreat extends FightOption
}

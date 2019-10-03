package joguin.game.step.fight

sealed abstract class FightOption extends Product with Serializable
case object FightAliens extends FightOption
case object Retreat extends FightOption

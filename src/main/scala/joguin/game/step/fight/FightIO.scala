package joguin.game.step.fight

import cats.effect.IO
import cats.~>
import joguin.playerinteraction.MessagesAndInteractionIO
import joguin.playerinteraction.wait.WaitIOThreadSleep

/** FightF composite interpreter to IO */
object FightIO {
  val composite: FightF ~> IO =
    MessagesAndInteractionIO.composite or
    WaitIOThreadSleep
}

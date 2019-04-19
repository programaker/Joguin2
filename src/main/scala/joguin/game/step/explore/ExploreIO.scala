package joguin.game.step.explore

import cats.effect.IO
import cats.~>
import joguin.playerinteraction.MessagesAndInteractionIO
import joguin.playerinteraction.wait.WaitIOThreadSleep

/** ExploreF composite interpreter to IO */
object ExploreIO {
  val composite: ExploreF ~> IO =
    MessagesAndInteractionIO.composite or
    WaitIOThreadSleep
}

package joguin.game.step

import cats.data.EitherK
import eu.timepit.refined.W
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal
import joguin.playerinteraction.MessagesAndInteractionF
import joguin.playerinteraction.wait.WaitF

package object fight {
  type FightOptionR = Equal[W.`"f"`.T] Or Equal[W.`"r"`.T]
  type FightF[A] = EitherK[MessagesAndInteractionF, WaitF, A]
}

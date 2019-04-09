package joguin.game.step

import cats.data.EitherK
import eu.timepit.refined.W
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.{MessageSourceF, MessagesF}
import joguin.playerinteraction.wait.WaitF

package object fight {
  type FightF[A] = EitherK[MessageSourceF, EitherK[MessagesF, EitherK[InteractionF, WaitF, ?], ?], A]
  type FightAliensOrRetreat = Equal[W.`"f"`.T] Or Equal[W.`"r"`.T]
}

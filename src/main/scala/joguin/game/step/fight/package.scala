package joguin.game.step

import cats.data.EitherK
import eu.timepit.refined.W
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.{MessageSourceF, MessagesF}
import joguin.playerinteraction.wait.WaitF

package object fight {
  type F1[A] = EitherK[MessageSourceF, MessagesF, A]
  type F2[A] = EitherK[F1, InteractionF, A]
  type FightF[A] = EitherK[F2, WaitF, A]

  type FightAliensOrRetreat = Equal[W.`"f"`.T] Or Equal[W.`"r"`.T]
}

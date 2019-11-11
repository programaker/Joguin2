package joguin.game.step.fight

import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.MessagesOps
import joguin.playerinteraction.wait.WaitOps

final class FightStepEnv[F[_]](
  m: MessagesOps[F],
  i: InteractionOps[F],
  w: WaitOps[F]
) {
  implicit val messageOps: MessagesOps[F] = m
  implicit val interactionOps: InteractionOps[F] = i
  implicit val waitOps: WaitOps[F] = w
}

object FightStepEnv {
  implicit def fightStepEnv[F[_]](
    implicit
    m: MessagesOps[F],
    i: InteractionOps[F],
    w: WaitOps[F]
  ): FightStepEnv[F] = new FightStepEnv[F](m, i, w)
}

package joguin.game.step.explore

import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.MessagesOps
import joguin.playerinteraction.wait.WaitOps

final class ExploreStepEnv[F[_]](
  m: MessagesOps[F],
  i: InteractionOps[F],
  w: WaitOps[F]
) {
  implicit val M: MessagesOps[F] = m
  implicit val I: InteractionOps[F] = i
  implicit val W: WaitOps[F] = w
}

object ExploreStepEnv {
  implicit def exploreStepEnv[F[_]](
    implicit
    m: MessagesOps[F],
    i: InteractionOps[F],
    w: WaitOps[F]
  ): ExploreStepEnv[F] = new ExploreStepEnv[F](m, i, w)
}

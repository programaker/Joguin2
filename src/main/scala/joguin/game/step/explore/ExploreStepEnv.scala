package joguin.game.step.explore

import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.MessagesOps
import joguin.playerinteraction.wait.WaitOps

final class ExploreStepEnv[F[_]](
  m: MessagesOps[F],
  i: InteractionOps[F],
  w: WaitOps[F]
) {
  implicit val messageOps: MessagesOps[F] = m
  implicit val interactionOps: InteractionOps[F] = i
  implicit val waitOps: WaitOps[F] = w
}

object ExploreStepEnv {
  implicit def exploreStepEnv[F[_]](
    implicit
    m: MessagesOps[F],
    i: InteractionOps[F],
    w: WaitOps[F]
  ): ExploreStepEnv[F] = new ExploreStepEnv[F](m, i, w)
}

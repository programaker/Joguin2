package joguin.game.step.showintro

import joguin.game.progress.GameProgressRepositoryOps
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.MessagesOps

final class ShowIntroStepEnv[F[_]](
  i: InteractionOps[F],
  m: MessagesOps[F],
  r: GameProgressRepositoryOps[F]
) {
  implicit val interactionOps: InteractionOps[F] = i
  implicit val messageOps: MessagesOps[F] = m
  implicit val gameProgressRepositoryOps: GameProgressRepositoryOps[F] = r
}

object ShowIntroStepEnv {
  implicit def showIntroStepEnv[F[_]](
    implicit
    i: InteractionOps[F],
    m: MessagesOps[F],
    r: GameProgressRepositoryOps[F]
  ): ShowIntroStepEnv[F] = new ShowIntroStepEnv[F](i, m, r)
}

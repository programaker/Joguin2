package joguin.game.step.savegame

import joguin.game.progress.GameProgressRepositoryOps
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.MessagesOps

final class SaveGameStepEnv[F[_]](
  i: InteractionOps[F],
  m: MessagesOps[F],
  r: GameProgressRepositoryOps[F]
) {
  implicit val interactionOps: InteractionOps[F] = i
  implicit val messageOps: MessagesOps[F] = m
  implicit val gameProgressRepositoryOps: GameProgressRepositoryOps[F] = r
}

object SaveGameStepEnv {
  implicit def saveGameStepEnv[F[_]](
    implicit
    i: InteractionOps[F],
    m: MessagesOps[F],
    r: GameProgressRepositoryOps[F]
  ): SaveGameStepEnv[F] = new SaveGameStepEnv[F](i, m, r)
}

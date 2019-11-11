package joguin.game.step.quit

import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.MessagesOps

final class QuitStepEnv[F[_]](
  m: MessagesOps[F],
  i: InteractionOps[F]
) {
  implicit val messageOps: MessagesOps[F] = m
  implicit val interactionOps: InteractionOps[F] = i
}

object QuitStepEnv {
  implicit def quitStepEnv[F[_]](implicit m: MessagesOps[F], i: InteractionOps[F]): QuitStepEnv[F] =
    new QuitStepEnv[F](m, i)
}

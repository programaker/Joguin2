package joguin.game.step

import cats.data.EitherK
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.W
import joguin.game.progress.GameProgressRepositoryOp
import joguin.playerinteraction.interaction.InteractionOp
import joguin.playerinteraction.message.MessagesOp

package object showintro {
  type Ops1[A] = EitherK[MessagesOp,GameProgressRepositoryOp,A]
  type ShowIntroOp[A] = EitherK[InteractionOp,Ops1,A]

  type ShowIntroAnswers = MatchesRegex[W.`"""^[nqr]$"""`.T]
  type ShowIntroAnswersNoRestore = MatchesRegex[W.`"""^[nq]$"""`.T]
}

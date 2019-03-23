package joguin.game.step

import cats.data.EitherK
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.W
import joguin.game.progress.GameProgressRepositoryF
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.{MessageSourceF, MessagesF}

package object showintro {
  type F1[A] = EitherK[MessagesF,GameProgressRepositoryF,A]
  type F2[A] = EitherK[F1,MessageSourceF,A]
  type ShowIntroF[A] = EitherK[InteractionF,F2,A]

  type ShowIntroAnswers = MatchesRegex[W.`"""^[nqr]$"""`.T]
  type ShowIntroAnswersNoRestore = MatchesRegex[W.`"""^[nq]$"""`.T]
}

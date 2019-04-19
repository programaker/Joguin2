package joguin.game.step

import cats.data.EitherK
import eu.timepit.refined.W
import eu.timepit.refined.string.MatchesRegex
import joguin.game.progress.GameProgressRepositoryF
import joguin.playerinteraction.message.MessagesAndSourceF

package object showintro {
  type ShowIntroOptionR = MatchesRegex[W.`"""^[nqr]$"""`.T]
  type ShowIntroOptionNoRestoreR = MatchesRegex[W.`"""^[nq]$"""`.T]
  type ShowIntroF[A] = EitherK[MessagesAndSourceF, GameProgressRepositoryF, A]
}

package joguin.game.step

import cats.data.EitherK
import eu.timepit.refined.W
import eu.timepit.refined.string.MatchesRegex
import joguin.game.progress.GameProgressRepositoryF
import joguin.playerinteraction.message.{MessageSourceF, MessagesF}

package object showintro {
  type ShowIntroF[A] =
    EitherK[MessageSourceF,
      EitherK[MessagesF,
        GameProgressRepositoryF, ?], A]

  type ShowIntroOptions = MatchesRegex[W.`"""^[nqr]$"""`.T]
  type ShowIntroOptionsNoRestore = MatchesRegex[W.`"""^[nq]$"""`.T]
}

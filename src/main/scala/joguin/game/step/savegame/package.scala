package joguin.game.step

import cats.data.EitherK
import joguin.game.progress.GameProgressRepositoryF
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.{MessageSourceF, MessagesF}

package object savegame {
  type SaveGameF[A] =
    EitherK[MessageSourceF,
      EitherK[MessagesF,
        GameProgressRepositoryF, ?], A]
}

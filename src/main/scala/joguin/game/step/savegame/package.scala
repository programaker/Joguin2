package joguin.game.step

import cats.data.EitherK
import joguin.game.progress.GameProgressRepositoryF
import joguin.playerinteraction.message.MessagesAndSourceF

package object savegame {
  type SaveGameF[A] = EitherK[MessagesAndSourceF, GameProgressRepositoryF, A]
}

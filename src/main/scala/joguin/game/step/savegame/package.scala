package joguin.game.step
import cats.data.EitherK
import joguin.game.progress.GameProgressRepositoryF
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.{MessageSourceF, MessagesF}

package object savegame {
  type F1[A] = EitherK[MessagesF, GameProgressRepositoryF, A]
  type F2[A] = EitherK[F1, MessageSourceF, A]
  type SaveGameF[A] = EitherK[InteractionF, F2, A]
}

package joguin

import cats.data.EitherK
import joguin.alien.terraformdevice.PowerGeneratorF
import joguin.earth.city.CityRepositoryF
import joguin.game.progress.GameProgressRepositoryF
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.{MessageSourceF, MessagesF}
import joguin.playerinteraction.wait.WaitF

package object game {
  type F1[A] = EitherK[MessagesF, MessageSourceF, A]
  type F2[A] = EitherK[F1, InteractionF, A]
  type F3[A] = EitherK[F2, CityRepositoryF, A]
  type F4[A] = EitherK[F3, GameProgressRepositoryF, A]
  type F5[A] = EitherK[F4, PowerGeneratorF, A]
  type GameF[A] = EitherK[F5, WaitF, A]
}

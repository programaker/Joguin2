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
  type F2[A] = EitherK[InteractionF, F1, A]
  type F3[A] = EitherK[CityRepositoryF, F2, A]
  type F4[A] = EitherK[GameProgressRepositoryF, F3, A]
  type F5[A] = EitherK[PowerGeneratorF, F4, A]
  type GameF[A] = EitherK[WaitF, F5, A]
}

package joguin.game.step

import cats.data.EitherK
import joguin.alien.terraformdevice.PowerGeneratorF
import joguin.earth.city.CityRepositoryF
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.{MessageSourceF, MessagesF}

package object createcharacter {
  type F1[A] = EitherK[MessageSourceF, MessagesF, A]
  type F2[A] = EitherK[F1, InteractionF, A]
  type F3[A] = EitherK[F2, PowerGeneratorF, A]
  type CreateCharacterF[A] = EitherK[F3, CityRepositoryF, A]
}

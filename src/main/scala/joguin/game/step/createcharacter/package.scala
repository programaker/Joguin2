package joguin.game.step

import cats.data.EitherK
import joguin.alien.terraformdevice.PowerGeneratorF
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.{MessageSourceF, MessagesF}

package object createcharacter {
  type CreateCharacterF[A] =
    EitherK[MessageSourceF,
      EitherK[MessagesF,
        EitherK[InteractionF,
          PowerGeneratorF, ?], ?], A]
}

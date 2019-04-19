package joguin.game.step

import cats.data.EitherK
import joguin.alien.terraformdevice.PowerGeneratorF
import joguin.playerinteraction.MessagesAndInteractionF

package object createcharacter {
  type CreateCharacterF[A] = EitherK[MessagesAndInteractionF, PowerGeneratorF, A]
}

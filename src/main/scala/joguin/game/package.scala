package joguin

import cats.data.EitherK
import joguin.game.step.createcharacter.CreateCharacterF
import joguin.game.step.explore.ExploreF
import joguin.game.step.fight.FightF
import joguin.game.step.quit.QuitF
import joguin.game.step.savegame.SaveGameF
import joguin.game.step.showintro.ShowIntroF

package object game {
  type F1[A] = EitherK[ShowIntroF, CreateCharacterF, A]
  type F2[A] = EitherK[F1, ExploreF, A]
  type F3[A] = EitherK[F2, FightF, A]
  type F4[A] = EitherK[F3, SaveGameF, A]
  type GameF[A] = EitherK[F4, QuitF, A]
}

package joguin.game

import cats.effect.IO
import cats.~>
import joguin.game.progress.GameProgressRepositoryF
import joguin.game.step.createcharacter.CreateCharacterIO
import joguin.game.step.explore.ExploreIO
import joguin.game.step.fight.FightIO
import joguin.game.step.quit.QuitIO
import joguin.game.step.savegame.SaveGameIO
import joguin.game.step.showintro.ShowIntroIO

/** GameF composite interpreter to IO */
object GameIO {
  def composite(gameProgressRepository: GameProgressRepositoryF ~> IO): GameF ~> IO =
    ShowIntroIO.composite(gameProgressRepository) or
    CreateCharacterIO.composite or
    ExploreIO.composite or
    FightIO.composite or
    SaveGameIO.composite(gameProgressRepository) or
    QuitIO.composite


}

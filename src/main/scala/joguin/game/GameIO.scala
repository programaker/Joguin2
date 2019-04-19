package joguin.game

import java.io.File

import cats.effect.IO
import cats.~>
import joguin.game.step.createcharacter.CreateCharacterIO
import joguin.game.step.explore.ExploreIO
import joguin.game.step.fight.FightIO
import joguin.game.step.quit.QuitIO
import joguin.game.step.savegame.SaveGameIO
import joguin.game.step.showintro.ShowIntroIO

/** GameF composite interpreter to IO */
object GameIO {
  def composite(file: File): GameF ~> IO =
    ShowIntroIO.composite(file) or
    CreateCharacterIO.composite or
    ExploreIO.composite or
    FightIO.composite or
    SaveGameIO.composite(file) or
    QuitIO.composite
}

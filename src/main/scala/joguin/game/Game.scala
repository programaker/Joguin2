package joguin.game

import cats.free.Free
import cats.free.Free._
import joguin.game.step.{CreateCharacter, Explore, Fight, GameOver, GameStep, Quit, SaveGame, ShowIntro}
import joguin.game.step.createcharacter.CreateCharacterStep
import joguin.game.step.explore.ExploreStep
import joguin.game.step.fight.FightStep
import joguin.game.step.quit.QuitStep
import joguin.game.step.savegame.SaveGameStep
import joguin.game.step.showintro.ShowIntroStep

final class Game(
  implicit
  showIntro: ShowIntroStep[GameF],
  createCharacter: CreateCharacterStep[GameF],
  explore: ExploreStep[GameF],
  fight: FightStep[GameF],
  saveGame: SaveGameStep[GameF],
  quit: QuitStep[GameF]
) {
  def play: Free[GameF, Unit] = play(ShowIntro)

  private def play(step: GameStep): Free[GameF, Unit] = step match {
    case ShowIntro =>
      showIntro.start.flatMap(play)

    case CreateCharacter =>
      createCharacter.start.flatMap(play)

    case Explore(gameProgress) =>
      explore.start(gameProgress).flatMap(play)

    case Fight(gameProgress, selectedInvasion) =>
      fight.start(gameProgress, selectedInvasion).flatMap(play)

    case SaveGame(gameProgress) =>
      saveGame.start(gameProgress).flatMap(play)

    case Quit(gameProgress) =>
      quit.start(gameProgress).flatMap(play)

    case GameOver =>
      pure(())
  }
}
object Game {
  def play(
    implicit
    showIntro: ShowIntroStep[GameF],
    createCharacter: CreateCharacterStep[GameF],
    explore: ExploreStep[GameF],
    fight: FightStep[GameF],
    saveGame: SaveGameStep[GameF],
    quit: QuitStep[GameF]
  ): Free[GameF, Unit] = {
    new Game().play
  }
}


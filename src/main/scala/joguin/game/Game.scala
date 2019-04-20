package joguin.game

import cats.free.Free
import cats.free.Free._
import joguin.alien.terraformdevice.PowerGeneratorOps
import joguin.earth.city.CityRepositoryOps
import joguin.game.progress.GameProgressRepositoryOps
import joguin.game.step.createcharacter.CreateCharacterStep
import joguin.game.step.explore.ExploreStep
import joguin.game.step.fight.FightStep
import joguin.game.step.quit.QuitStep
import joguin.game.step.savegame.SaveGameStep
import joguin.game.step.showintro.ShowIntroStep
import joguin.game.step._
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.{MessageSourceOps, MessagesOps}
import joguin.playerinteraction.wait.WaitOps

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
    messageOps: MessagesOps[GameF],
    messageSourceOps: MessageSourceOps[GameF],
    interactionOps: InteractionOps[GameF],
    gameProgressRepositoryOps: GameProgressRepositoryOps[GameF],
    cityRepositoryOps: CityRepositoryOps[GameF],
    powerGeneratorOps: PowerGeneratorOps[GameF],
    waitOps: WaitOps[GameF]
  ): Free[GameF, Unit] = {

    implicit val showIntro: ShowIntroStep[GameF] = ShowIntroStep.showIntroStep[GameF]
    implicit val createCharacter: CreateCharacterStep[GameF] = CreateCharacterStep.createCharacterStep[GameF]
    implicit val explore: ExploreStep[GameF] = ExploreStep.exploreStep[GameF]
    implicit val fight: FightStep[GameF] = FightStep.fightStep[GameF]
    implicit val saveGame: SaveGameStep[GameF] = SaveGameStep.saveGameStep[GameF]
    implicit val quit: QuitStep[GameF] = QuitStep.quitStep[GameF]

    new Game().play
  }
}


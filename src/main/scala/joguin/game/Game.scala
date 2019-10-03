package joguin.game

import cats.free.Free
import cats.free.Free._
import joguin.alien.terraformdevice.PowerGeneratorOps
import joguin.earth.city.CityRepositoryOps
import joguin.game.progress.GameProgressRepositoryOps
import joguin.game.step._
import joguin.game.step.createcharacter.CreateCharacterStep
import joguin.game.step.explore.ExploreStep
import joguin.game.step.fight.FightStep
import joguin.game.step.quit._
import joguin.game.step.savegame._
import joguin.game.step.showintro.ShowIntroStep
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.MessageSourceOps
import joguin.playerinteraction.message.MessagesOps
import joguin.playerinteraction.wait.WaitOps

final class Game(
  showIntro: ShowIntroStep[GameF],
  createCharacter: CreateCharacterStep[GameF],
  explore: ExploreStep[GameF],
  fight: FightStep[GameF]
) {
  def play: Free[GameF, Unit] = gameLoop(ShowIntro)

  private def gameLoop(step: GameStep): Free[GameF, Unit] = step match {
    case ShowIntro =>
      showIntro.play.flatMap(gameLoop)

    case CreateCharacter =>
      createCharacter.play.flatMap(gameLoop)

    case Explore(gameProgress) =>
      explore.play(gameProgress).flatMap(gameLoop)

    case Fight(gameProgress, selectedInvasion) =>
      fight.play(gameProgress, selectedInvasion).flatMap(gameLoop)

    case SaveGame(gameProgress) =>
      playSaveGame[GameF](gameProgress).flatMap(gameLoop)

    case Quit(gameProgress) =>
      playQuitStep[GameF](gameProgress).flatMap(gameLoop)

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
  ): Free[GameF, Unit] =
    new Game(
      new ShowIntroStep[GameF],
      new CreateCharacterStep[GameF],
      new ExploreStep[GameF],
      new FightStep[GameF]
    ).play
}

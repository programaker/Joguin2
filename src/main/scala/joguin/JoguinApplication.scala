package joguin

import java.io.File

import cats.InjectK
import cats.free.Free
import cats.free.Free._
import cats.effect.{ExitCode, IO, IOApp}
import joguin.game._
import joguin.game.progress.GameProgressRepositoryIOFile
import cats.implicits._
import joguin.playerinteraction.message.MessageSourceOps._
import joguin.playerinteraction.message.MessagesOps._
import joguin.playerinteraction.interaction.InteractionOps._
import joguin.playerinteraction.wait.WaitOps._
import joguin.game.progress.GameProgressRepositoryOps._
import joguin.alien.terraformdevice.PowerGeneratorOps._
import joguin.game.step.showintro.ShowIntroStep._
import joguin.game.step.createcharacter.CreateCharacterStep._
import joguin.game.step.explore.ExploreStep._
import joguin.game.step.fight.FightStep._
import joguin.game.step.savegame.SaveGameStep._
import joguin.game.step.quit.QuitStep._

object JoguinApplication extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val gameProgressRepository = GameProgressRepositoryIOFile(new File("saved-game/last-progress.prog"))
    val gameIO = GameIO.composite(gameProgressRepository)

    Game.play
      .foldMap(gameIO)
      .as(ExitCode.Success)
  }
}

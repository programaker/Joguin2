package joguin

import java.io.File

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import joguin.alien.terraformdevice.PowerGeneratorOps._
import joguin.earth.city.CityRepositoryOps._
import joguin.game._
import joguin.game.progress.GameProgressRepositoryIOFile
import joguin.game.progress.GameProgressRepositoryOps._
import joguin.playerinteraction.interaction.InteractionOps._
import joguin.playerinteraction.message.MessageSourceOps._
import joguin.playerinteraction.message.MessagesOps._
import joguin.playerinteraction.wait.WaitOps._

object JoguinApplication extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val gameProgressRepository = GameProgressRepositoryIOFile(new File("saved-game/last-progress.prog"))
    val gameIO = GameIO.composite(gameProgressRepository)

    Game.play
      .foldMap(gameIO)
      .as(ExitCode.Success)
  }
}

package joguin

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import joguin.alien.terraformdevice.PowerGeneratorOps._
import joguin.earth.city.CityRepositoryOps._
import joguin.game._
import joguin.game.progress.GameProgressRepositoryOps._
import joguin.playerinteraction.interaction.InteractionOps._
import joguin.playerinteraction.message.MessageSourceOps._
import joguin.playerinteraction.message.MessagesOps._
import joguin.playerinteraction.wait.WaitOps._

object JoguinApplication extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    Game.play
      .foldMap(GameIOInterpreter.build)
      .map(_ => ExitCode.Success)
      .handleErrorWith(_ => IO.pure(ExitCode.Error))
  }
}

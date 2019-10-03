package joguin

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import joguin.game._

object JoguinApplication extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    playGame
      .foldMap(gameInterpreter[IO](saveProgressFile = "saved-game/last-progress.prog"))
      .map(_ => ExitCode.Success)
      .handleErrorWith(_ => IO.pure(ExitCode.Error))
}

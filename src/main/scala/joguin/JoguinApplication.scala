package joguin

import joguin.Recovery._
import joguin.game._
import zio.Task
import zio.ZIO

object JoguinApplication extends zio.App {
  override def run(args: List[String]): ZIO[JoguinApplication.Environment, Nothing, Int] = {
    val saveProgressFile = "saved-game/last-progress.prog"

    playGame
      .foldMap(gameInterpreter[Task](saveProgressFile))
      .map(_ => 0)
      .catchAll(_ => Task.succeed(1))
  }
}

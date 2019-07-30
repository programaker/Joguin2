package joguin

import joguin.alien.terraformdevice.PowerGeneratorOps._
import joguin.earth.city.CityRepositoryOps._
import joguin.game._
import joguin.game.progress.GameProgressRepositoryOps._
import joguin.playerinteraction.interaction.InteractionOps._
import joguin.playerinteraction.message.MessageSourceOps._
import joguin.playerinteraction.message.MessagesOps._
import joguin.playerinteraction.wait.WaitOps._
import zio.Task
import zio.ZIO

object JoguinApplication extends zio.App {
  override def run(args: List[String]): ZIO[JoguinApplication.Environment, Nothing, Int] =
    Game.play
      .foldMap(GameInterpreter[Task].build)
      .map(_ => 0)
      .catchAll(_ => Task.succeed(1))
}

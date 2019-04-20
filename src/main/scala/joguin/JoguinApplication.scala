package joguin

import java.io.File

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import joguin.alien.terraformdevice.PowerGeneratorOps
import joguin.alien.terraformdevice.PowerGeneratorOps._
import joguin.earth.city.CityRepositoryOps
import joguin.earth.city.CityRepositoryOps._
import joguin.game._
import joguin.game.progress.GameProgressRepositoryOps._
import joguin.game.progress.{GameProgressRepositoryIOFile, GameProgressRepositoryOps}
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.interaction.InteractionOps._
import joguin.playerinteraction.message.MessageSourceOps._
import joguin.playerinteraction.message.MessagesOps._
import joguin.playerinteraction.message.{MessageSourceOps, MessagesOps}
import joguin.playerinteraction.wait.WaitOps._


object JoguinApplication extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {

    //implicit val i1: MessagesOps[GameF] = messagesOps[GameF]
    //implicit val i2: MessageSourceOps[GameF] = messageSourceOps[GameF]
    //implicit val i3: InteractionOps[GameF] = interactionOps[GameF]
    implicit val i4: GameProgressRepositoryOps[GameF] = gameProgressRepositoryOps[GameF]
    //implicit val i5: CityRepositoryOps[GameF] = cityRepositoryOps[GameF]
    //implicit val i6: PowerGeneratorOps[GameF] = powerGeneratorOps[GameF]
    //implicit val i7: WaitOps[GameF] = waitOps[GameF]

    val gameProgressRepository = GameProgressRepositoryIOFile(new File("saved-game/last-progress.prog"))
    val gameIO = GameIO.composite(gameProgressRepository)

    Game.play
      .foldMap(gameIO)
      .as(ExitCode.Success)
  }
}

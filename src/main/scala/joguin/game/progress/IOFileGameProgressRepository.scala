package joguin.game.progress

import java.io.File
import java.nio.charset.StandardCharsets._

import cats.effect.IO
import cats.~>
import eu.timepit.refined.numeric.{NonNegative, Positive}
import io.circe.Decoder.Result
import org.apache.commons.io.FileUtils
import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser._
import io.circe.syntax._
import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter

class IOFileGameProgressRepository(val file: File) extends (GameProgressRepositoryOp ~> IO) {
  override def apply[A](op: GameProgressRepositoryOp[A]): IO[A] = op match {
    case Save(gameProgress) => save(gameProgress)
    case SavedProgressExists => savedProgressExists
    case Restore => restore
  }

  private def save(gameProgress: GameProgress): IO[Unit] = for {
    _ <- mkdirs
    _ <- writeToFile(gameProgress)
  } yield ()


  private def savedProgressExists: IO[Boolean] =
    IO(file.exists())

  private def restore: IO[Option[GameProgress]] =
    savedProgressExists.flatMap {
      case false => IO.pure(None)
      case true => readFile
    }

  private def mkdirs: IO[Unit] =
    IO(FileUtils.forceMkdirParent(file))

  private def writeToFile(gameProgress: GameProgress): IO[Unit] = ???
    /*IO.pure(gameProgress)
      .map(_.asJson.noSpaces)
      .flatMap(json => IO(FileUtils.write(file, json, UTF_8)))*/

  private def readFile: IO[Option[GameProgress]] = ???
    /*IO(FileUtils.readFileToString(file, UTF_8))
      .map(decode[GameProgress])
      .map(_.toOption)*/
}

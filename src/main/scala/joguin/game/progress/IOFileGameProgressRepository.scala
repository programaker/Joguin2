package joguin.game.progress

import java.io.File
import java.nio.charset.StandardCharsets._

import cats.effect.IO
import cats.~>
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import joguin.game.progress.PersistentGameProgress._
import org.apache.commons.io.FileUtils

final class IOFileGameProgressRepository(val file: File) extends (GameProgressRepositoryF ~> IO) {
  override def apply[A](fa: GameProgressRepositoryF[A]): IO[A] = fa match {
    case Save(gameProgress) => save(gameProgress)
    case SavedProgressExists => savedProgressExists
    case Restore => restore
  }

  private def save(gameProgress: GameProgress): IO[Boolean] =
    mkdirs.flatMap(_ => writeToFile(gameProgress))

  private def savedProgressExists: IO[Boolean] =
    IO(file.exists())

  private def restore: IO[Option[GameProgress]] =
    savedProgressExists.flatMap { exists =>
      if (exists) {
        readFile
      } else {
        IO.pure(None)
      }
    }

  private def mkdirs: IO[Unit] =
    IO(FileUtils.forceMkdirParent(file))

  private def writeToFile(gameProgress: GameProgress): IO[Boolean] =
    IO.pure(gameProgress)
      .map(fromGameProgress)
      .map(_.asJson.noSpaces)
      .flatMap(json => IO(FileUtils.write(file, json, UTF_8)))
      .map(_ => true)
      .handleErrorWith(_ => IO.pure(false))

  private def readFile: IO[Option[GameProgress]] =
    IO(FileUtils.readFileToString(file, UTF_8))
      .map(decode[PersistentGameProgress])
      .map(_.map(toGameProgress))
      .map(_.getOrElse(None))
}

package joguin.game.progress

import java.io.File
import java.nio.charset.StandardCharsets._

import cats.implicits._
import cats.~>
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import joguin.Lazy
import joguin.Recovery
import joguin.game.progress.PersistentGameProgress._
import org.apache.commons.io.FileUtils

/** GameProgressRepositoryF root interpreter to any F that uses a file for persistence */
final class GameProgressRepositoryInterpreter[F[_]: Recovery: Lazy](val file: File)
    extends (GameProgressRepositoryF ~> F) {

  override def apply[A](fa: GameProgressRepositoryF[A]): F[A] = fa match {
    case Save(gameProgress)  => save(gameProgress)
    case SavedProgressExists => savedProgressExists
    case Restore             => restore
  }

  private def save(gameProgress: GameProgress): F[Boolean] =
    mkdirs.flatMap(_ => writeToFile(gameProgress))

  private def savedProgressExists: F[Boolean] =
    Lazy[F].lift(file.exists())

  private def restore: F[Option[GameProgress]] =
    savedProgressExists.flatMap { exists =>
      if (exists) {
        readFile
      } else {
        Recovery[F].pure(None)
      }
    }

  private def mkdirs: F[Unit] =
    Lazy[F].lift(FileUtils.forceMkdirParent(file))

  private def writeToFile(gameProgress: GameProgress): F[Boolean] =
    Recovery[F]
      .pure(gameProgress)
      .map(fromGameProgress)
      .map(_.asJson.noSpaces)
      .flatMap(json => Lazy[F].lift(FileUtils.write(file, json, UTF_8)))
      .map(_ => true)
      .handleError(_ => false)

  private def readFile: F[Option[GameProgress]] =
    Lazy[F]
      .lift(FileUtils.readFileToString(file, UTF_8))
      .map(decode[PersistentGameProgress])
      .map(_.map(_.toGameProgress))
      .map(_.getOrElse(None))
}

object GameProgressRepositoryInterpreter {
  def apply[F[_]: Recovery: Lazy](file: File): GameProgressRepositoryInterpreter[F] =
    new GameProgressRepositoryInterpreter[F](file)
}

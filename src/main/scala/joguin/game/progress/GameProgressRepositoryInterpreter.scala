package joguin.game.progress

import java.io.File
import java.nio.charset.StandardCharsets._

import cats.implicits._
import cats.~>
import io.circe.generic.auto._
import io.circe.jawn
import io.circe.syntax._
import joguin.Lazy
import joguin.Recovery
import joguin.game.progress.GameProgressRepositoryF._
import org.apache.commons.io.FileUtils

/** GameProgressRepositoryF root interpreter to any F that uses a file for persistence */
final class GameProgressRepositoryInterpreter[F[_]: Recovery: Lazy](file: File) extends (GameProgressRepositoryF ~> F) {
  //Despite IntelliJ telling that `import io.circe.refined._` is not being used,
  //it is required to make Circe work with Refined Types
  import io.circe.refined._

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
      .map(_.asJson.noSpaces)
      .flatMap(json => Lazy[F].lift(FileUtils.write(file, json, UTF_8)))
      .map(_ => true)
      .handleError(_ => false)

  private def readFile: F[Option[GameProgress]] =
    Lazy[F]
      .lift(FileUtils.readFileToString(file, UTF_8))
      .map(jawn.decode[GameProgress])
      .map(_.toOption)
}

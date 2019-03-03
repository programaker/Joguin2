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
  import IOFileGameProgressRepository._

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

  private def writeToFile(gameProgress: GameProgress): IO[Unit] =
    IO.pure(gameProgress)
      .map(_.asJson.noSpaces)
      .flatMap(json => IO(FileUtils.write(file, json, UTF_8)))

  private def readFile: IO[Option[GameProgress]] =
    IO(FileUtils.readFileToString(file, UTF_8))
      .map(decode[GameProgress])
      .map(_.toOption)
}

object IOFileGameProgressRepository {
  import cats.implicits._
  import io.circe.generic.auto._
  import eu.timepit.refined._
  import eu.timepit.refined.auto._

  implicit val encoder: Encoder[GameProgress] = new Encoder[GameProgress] {
    override def apply(gp: GameProgress): Json = Json.obj(
      "mainCharacter" -> gp.mainCharacter.asJson,
      "mainCharacterExperience" -> Json.fromInt(gp.mainCharacterExperience),
      "invasions" -> gp.invasions.asJson,
      "defeatedInvasions" -> Json.fromInt(gp.defeatedInvasions),
      "defeatedInvasionsTrack" -> gp.defeatedInvasionsTrack.asJson
    )
  }

  implicit val decoder: Decoder[GameProgress] = new Decoder[GameProgress] {
    override def apply(c: HCursor): Result[GameProgress] = for {
      mainCharacter <- c.downField("mainCharacter").as[MainCharacter]
      mainCharacterExperience <- c.downField("mainCharacterExperience").as[Int]
      invasions <- c.downField("invasions").as[List[Invasion]]
      defeatedInvasions <- c.downField("defeatedInvasions").as[Int]
      defeatedInvasionsTrack <- c.downField("defeatedInvasionsTrack").as[Set[Int]]
    } yield {
      dec(mainCharacter, mainCharacterExperience, invasions, defeatedInvasions, defeatedInvasionsTrack)
    }

    private def dec(
        mainCharacter: MainCharacter,
        mainCharacterExperience: Int,
        invasions: List[Invasion],
        defeatedInvasions: Int,
        defeatedInvasionsTrack: Set[Int]): Result[GameProgress] = {

      val refExperience = refineV[NonNegative](mainCharacterExperience)
      val refDefeatedInvasions = refineV[NonNegative](defeatedInvasions)

      val refDefeatedInvasionsTrack = defeatedInvasionsTrack
        .toStream
        .map(refineV[Positive](_))
        .map(_.toOption)
        .toSet
        .sequence
        .getOrElse(Set.empty)
    }
  }
}

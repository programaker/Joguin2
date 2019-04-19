package joguin.game.step.showintro

import java.io.File

import cats.effect.IO
import cats.~>
import joguin.game.progress.GameProgressRepositoryIOFile
import joguin.playerinteraction.message.MessagesAndSourceIO

/** ShowIntroF composite interpreter to IO */
object ShowIntroIO {
  def composite(file: File): ShowIntroF ~> IO =
    MessagesAndSourceIO.composite or
    GameProgressRepositoryIOFile(file)
}

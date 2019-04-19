package joguin.game.step.savegame

import java.io.File

import cats.effect.IO
import cats.~>
import joguin.game.progress.GameProgressRepositoryIOFile
import joguin.playerinteraction.message.MessagesAndSourceIO

/** SaveGameF composite interpreter to IO */
object SaveGameIO {
  def composite(file: File): SaveGameF ~> IO =
    MessagesAndSourceIO.composite or
    GameProgressRepositoryIOFile(file)
}

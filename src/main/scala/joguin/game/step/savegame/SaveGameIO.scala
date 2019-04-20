package joguin.game.step.savegame

import cats.effect.IO
import cats.~>
import joguin.game.progress.GameProgressRepositoryF
import joguin.playerinteraction.message.MessagesAndSourceIO

/** SaveGameF composite interpreter to IO */
object SaveGameIO {
  def composite(gameProgressRepository: GameProgressRepositoryF ~> IO): SaveGameF ~> IO =
    MessagesAndSourceIO.composite or
    gameProgressRepository
}

package joguin.game.step.showintro

import cats.effect.IO
import cats.~>
import joguin.game.progress.GameProgressRepositoryF
import joguin.playerinteraction.message.MessagesAndSourceIO

/** ShowIntroF composite interpreter to IO */
object ShowIntroIO {
  def composite(gameProgressRepository: GameProgressRepositoryF ~> IO): ShowIntroF ~> IO =
    MessagesAndSourceIO.composite or
    gameProgressRepository
}

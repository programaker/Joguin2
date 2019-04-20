package joguin.game.progress

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait GameProgressRepositoryF[A]
final case class Save(gameProgress: GameProgress) extends GameProgressRepositoryF[Boolean]
case object SavedProgressExists extends GameProgressRepositoryF[Boolean]
case object Restore extends GameProgressRepositoryF[Option[GameProgress]]

final class GameProgressRepositoryOps[C[_]](implicit i: InjectK[GameProgressRepositoryF, C]) {
  def save(gameProgress: GameProgress): Free[C, Boolean] = inject(Save(gameProgress))
  def savedProgressExists: Free[C, Boolean] = inject(SavedProgressExists)
  def restore: Free[C, Option[GameProgress]] = inject(Restore)
}
object GameProgressRepositoryOps {
  implicit def gameProgressRepositoryOps[C[_]](
    implicit i: InjectK[GameProgressRepositoryF, C]
  ): GameProgressRepositoryOps[C] = {
    new GameProgressRepositoryOps[C]
  }
}

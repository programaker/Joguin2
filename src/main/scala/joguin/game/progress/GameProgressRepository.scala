package joguin.game.progress

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait GameProgressRepositoryF[A]
final case class Save(gameProgress: GameProgress) extends GameProgressRepositoryF[Boolean]
case object SavedProgressExists extends GameProgressRepositoryF[Boolean]
case object Restore extends GameProgressRepositoryF[Option[GameProgress]]

final class GameProgressRepositoryOps[G[_]](implicit I: InjectK[GameProgressRepositoryF, G]) {
  def save(gameProgress: GameProgress): Free[G, Boolean] = inject(Save(gameProgress))
  def savedProgressExists: Free[G, Boolean] = inject(SavedProgressExists)
  def restore: Free[G, Option[GameProgress]] = inject(Restore)
}
object GameProgressRepositoryOps {
  def create[G[_]](implicit I: InjectK[GameProgressRepositoryF, G]): GameProgressRepositoryOps[G] =
    new GameProgressRepositoryOps[G]
}

package joguin.game.progress

import cats.InjectK
import cats.free.Free
import cats.free.Free._


sealed trait GameProgressRepositoryOp[A]
final case class Save(gameProgress: GameProgress) extends GameProgressRepositoryOp[Unit]
case object SavedProgressExists extends GameProgressRepositoryOp[Boolean]
case object Restore extends GameProgressRepositoryOp[Option[GameProgress]]


final class GameProgressRepository[F[_]](implicit I: InjectK[GameProgressRepositoryOp,F]) {
  def save(gameProgress: GameProgress): Free[F,Unit] = inject(Save(gameProgress))
  def savedProgressExists: Free[F,Boolean] = inject(SavedProgressExists)
  def restore: Free[F,Option[GameProgress]] = inject(Restore)
}

object GameProgressRepository {
  def create[F[_]](implicit I: InjectK[GameProgressRepositoryOp,F]): GameProgressRepository[F] =
    new GameProgressRepository[F]
}
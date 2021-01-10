package joguin.game.progress

import cats.InjectK
import cats.free.Free
import cats.free.Free._
import joguin.game.progress.GameProgressRepositoryF._

sealed trait GameProgressRepositoryF[A]

object GameProgressRepositoryF {
  final case class Save(gameProgress: GameProgress) extends GameProgressRepositoryF[Boolean]
  case object SavedProgressExists extends GameProgressRepositoryF[Boolean]
  case object Restore extends GameProgressRepositoryF[Option[GameProgress]]
}

final class GameProgressRepositoryOps[F[_]](implicit i: InjectK[GameProgressRepositoryF, F]) {
  def save(gameProgress: GameProgress): Free[F, Boolean] = inject(Save(gameProgress))
  def savedProgressExists: Free[F, Boolean] = inject(SavedProgressExists)
  def restore: Free[F, Option[GameProgress]] = inject(Restore)
}

object GameProgressRepositoryOps {
  implicit def gameProgressRepositoryOps[F[_]](
    implicit i: InjectK[GameProgressRepositoryF, F]
  ): GameProgressRepositoryOps[F] =
    new GameProgressRepositoryOps[F]
}

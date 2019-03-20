package joguin.earth.city

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait CityRepositoryOp[T]
case object FindAll extends CityRepositoryOp[List[City]]

final class CityRepository[F[_]](implicit I: InjectK[CityRepositoryOp,F]) {
  def findAll: Free[F,List[City]] = inject[CityRepositoryOp,F](FindAll)
}

object CityRepository {
  implicit def create[F[_]](implicit I: InjectK[CityRepositoryOp,F]): CityRepository[F] = new CityRepository[F]
}

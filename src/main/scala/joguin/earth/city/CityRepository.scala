package joguin.earth.city

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait CityRepositoryF[T]
case object FindAll extends CityRepositoryF[List[City]]

class CityRepository[F[_]](implicit I: InjectK[CityRepositoryF,F]) {
  def findAll: Free[F,List[City]] = inject[CityRepositoryF,F](FindAll)
}

object CityRepository {
  implicit def create[F[_]](implicit I: InjectK[CityRepositoryF,F]): CityRepository[F] = new CityRepository[F]
}

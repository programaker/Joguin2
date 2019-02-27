package joguin.earth.city

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait CityRepositoryF[T]
case object FindAll extends CityRepositoryF[List[City]]

class CityRepository[C[_]](implicit I: InjectK[CityRepositoryF,C]) {
  def findAll: Free[C,List[City]] = inject[CityRepositoryF,C](FindAll)
}

object CityRepository {
  implicit def create[C[_]](implicit I: InjectK[CityRepositoryF,C]): CityRepository[C] = new CityRepository[C]
}

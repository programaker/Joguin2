package joguin.earth.city

import scalaz.Free._
import scalaz.{Free, Inject}

sealed trait CityRepositoryF[T]
case object FindAll extends CityRepositoryF[List[City]]

class CityRepository[C[_]](implicit i: Inject[CityRepositoryF,C]) {
  def findAll: Free[C,List[City]] = liftF(i.inj(FindAll))
}

object CityRepository {
  implicit def create[C[_]](implicit i: Inject[CityRepositoryF,C]): CityRepository[C] = new CityRepository[C]
}

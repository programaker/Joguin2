package joguin.earth.city

import scalaz.Free

sealed trait CityRepositoryF[T]
case object FindAll extends CityRepositoryF[List[City]]

object CityRepository {
  def findAll: CityRepository[List[City]] = Free.liftF(FindAll)
}

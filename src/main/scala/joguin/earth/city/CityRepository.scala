package joguin.earth.city

import scalaz.Free._

sealed trait CityRepositoryF[T]
case object FindAll extends CityRepositoryF[List[City]]

object CityRepository {
  def findAll: CityRepository[List[City]] = liftF(FindAll)
}

package joguin.earth.city

import joguin.CityRepository
import scalaz.Free

sealed trait CityRepositoryOps[T]
case object FindAll extends CityRepositoryOps[List[City]]

object CityRepository {
  def findAll: CityRepository[List[City]] = Free.liftF(FindAll)
}

package joguin.earth.city

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait CityRepositoryF[A]
case object FindAllCities extends CityRepositoryF[List[City]]

final class CityRepositoryOps[G[_]](implicit I: InjectK[CityRepositoryF, G]) {
  def findAllCities: Free[G, List[City]] = inject[CityRepositoryF, G](FindAllCities)
}
object CityRepositoryOps {
  implicit def create[G[_]](implicit I: InjectK[CityRepositoryF, G]): CityRepositoryOps[G] =
    new CityRepositoryOps[G]
}

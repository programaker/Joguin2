package joguin.earth.city

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait CityRepositoryF[A]
case object FindAllCities extends CityRepositoryF[List[City]]

final class CityRepositoryOps[C[_]](implicit i: InjectK[CityRepositoryF, C]) {
  def findAllCities: Free[C, List[City]] = inject[CityRepositoryF, C](FindAllCities)
}
object CityRepositoryOps {
  implicit def create[C[_]](implicit i: InjectK[CityRepositoryF, C]): CityRepositoryOps[C] =
    new CityRepositoryOps[C]
}

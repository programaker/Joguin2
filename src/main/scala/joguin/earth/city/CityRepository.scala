package joguin.earth.city

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed abstract class CityRepositoryF[A] extends Product with Serializable
case object FindAllCities extends CityRepositoryF[Vector[City]]

final class CityRepositoryOps[C[_]](implicit i: InjectK[CityRepositoryF, C]) {
  def findAllCities: Free[C, Vector[City]] = inject[CityRepositoryF, C](FindAllCities)
}

object CityRepositoryOps {
  implicit def cityRepositoryOps[C[_]](implicit i: InjectK[CityRepositoryF, C]): CityRepositoryOps[C] =
    new CityRepositoryOps[C]
}

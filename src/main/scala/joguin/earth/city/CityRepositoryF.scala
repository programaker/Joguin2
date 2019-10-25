package joguin.earth.city

import cats.InjectK
import cats.free.Free
import cats.free.Free._
import joguin.IdxSeq
import joguin.earth.city.CityRepositoryF.FindAllCities

sealed abstract class CityRepositoryF[A] extends Product with Serializable

object CityRepositoryF {
  case object FindAllCities extends CityRepositoryF[IdxSeq[City]]
}

final class CityRepositoryOps[C[_]](implicit i: InjectK[CityRepositoryF, C]) {
  def findAllCities: Free[C, IdxSeq[City]] = inject(FindAllCities)
}

object CityRepositoryOps {
  implicit def cityRepositoryOps[C[_]](implicit i: InjectK[CityRepositoryF, C]): CityRepositoryOps[C] =
    new CityRepositoryOps[C]
}

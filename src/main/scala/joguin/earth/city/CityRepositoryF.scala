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

final class CityRepositoryOps[F[_]](implicit i: InjectK[CityRepositoryF, F]) {
  def findAllCities: Free[F, IdxSeq[City]] = inject(FindAllCities)
}

object CityRepositoryOps {
  implicit def cityRepositoryOps[F[_]](implicit i: InjectK[CityRepositoryF, F]): CityRepositoryOps[F] =
    new CityRepositoryOps[F]
}

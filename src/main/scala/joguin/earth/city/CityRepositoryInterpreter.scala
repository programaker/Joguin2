package joguin.earth.city

import cats.Monad
import cats.~>
import eu.timepit.refined.auto._
import joguin.IdxSeq
import joguin.earth.city.CityRepositoryF.FindAllCities

/** CityRepositoryF interpreter to any Monad that returns a hardcoded list of cities.
 * Having no restriction about the target Monad, it can be used for both production and test */
final class CityRepositoryInterpreter[F[_]: Monad] extends (CityRepositoryF ~> F) {
  override def apply[A](fa: CityRepositoryF[A]): F[A] = fa match {
    case FindAllCities => allCities
  }

  private val allCities: F[IdxSeq[City]] = Monad[F].pure(
    IdxSeq(
      City("Berlin", "Germany"),
      City("Juiz de Fora", "Brazil"),
      City("Los Angeles", "USA"),
      City("Beijing", "China"),
      City("Nairobi", "Kenya"),
      City("Wellington", "New Zealand")
    )
  )
}

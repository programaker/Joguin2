package joguin.earth.city

import cats.effect.IO
import cats.~>
import eu.timepit.refined.auto._

/** CityRepositoryOp interpreter for IO that contains a hard-coded list of cities */
object IOHardcodedCityRepository extends (CityRepositoryF ~> IO) {
  override def apply[A](fa: CityRepositoryF[A]): IO[A] = fa match {
    case FindAllCities => allCities
  }

  private val allCities = IO.pure(List(
    City("Berlin", "Germany"),
    City("Juiz de Fora", "Brazil"),
    City("Los Angeles", "USA"),
    City("Beijing", "China"),
    City("Nairobi", "Kenya"),
    City("Wellington", "New Zealand")
  ))
}

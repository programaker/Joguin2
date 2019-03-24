package joguin.earth.city

import cats.{Id, ~>}
import eu.timepit.refined.auto._

/** CityRepositoryOp interpreter for Id that contains a hard-coded list of cities */
object IdHardcodedCityRepository extends (CityRepositoryF ~> Id) {
  override def apply[A](fa: CityRepositoryF[A]): Id[A] = fa match {
    case FindAllCities => allCities
  }

  private val allCities = List(
    City("Berlin", "Germany"),
    City("Juiz de Fora", "Brazil"),
    City("Los Angeles", "USA"),
    City("Beijing", "China"),
    City("Nairobi", "Kenya"),
    City("Wellington", "New Zealand")
  )
}

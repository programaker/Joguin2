package joguin.earth.city

import cats.{Id, ~>}
import eu.timepit.refined.auto._

object HardcodedCityRepository extends (CityRepositoryF ~> Id) {
  private val allCities = List(
    City("Berlin", "Germany"),
    City("Juiz de Fora", "Brazil"),
    City("Los Angeles", "USA"),
    City("Beijing", "China"),
    City("Nairobi", "Kenya"),
    City("Wellington", "New Zealand")
  )

  override def apply[A](fa: CityRepositoryF[A]): Id[A] = fa match {
    case FindAll => allCities
  }
}

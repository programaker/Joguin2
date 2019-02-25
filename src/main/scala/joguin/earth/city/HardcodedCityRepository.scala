package joguin.earth.city

import scalaz.{Id, ~>}
import eu.timepit.refined.auto._

object HardcodedCityRepository extends (CityRepositoryOps ~> Id.Id) {
  private val allCities = List(
    City("Berlin", "Germany"),
    City("Juiz de Fora", "Brazil"),
    City("Los Angeles", "USA"),
    City("Beijing", "China"),
    City("Nairobi", "Kenya"),
    City("Wellington", "New Zealand")
  )

  override def apply[A](fa: CityRepositoryOps[A]): Id.Id[A] = fa match {
    case FindAll => allCities
  }
}

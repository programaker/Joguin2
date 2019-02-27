package joguin.earth.city

import cats.{Id, ~>}
import eu.timepit.refined.auto._

object HardcodedCityRepository extends (CityRepositoryOp ~> Id) {
  private val allCities = List(
    City("Berlin", "Germany"),
    City("Juiz de Fora", "Brazil"),
    City("Los Angeles", "USA"),
    City("Beijing", "China"),
    City("Nairobi", "Kenya"),
    City("Wellington", "New Zealand")
  )

  override def apply[A](op: CityRepositoryOp[A]): Id[A] = op match {
    case FindAll => allCities
  }
}

package joguin.earth.city

import cats.{Id, ~>}
import eu.timepit.refined.auto._

/** CityRepositoryOp interpreter for Id that contains a hard-coded list of cities */
object IdHardcodedCityRepository extends (CityRepositoryOp ~> Id) {
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

package joguin.earth

import scalaz.Free

package object city {
  type CityRepository[A] = Free[CityRepositoryF,A]
}

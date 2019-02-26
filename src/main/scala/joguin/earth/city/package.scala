package joguin.earth

import scalaz.Free

package object city {
  type CityRepository[T] = Free[CityRepositoryF,T]
}

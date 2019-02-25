import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.{GreaterEqual, Positive}
import eu.timepit.refined.W
import eu.timepit.refined.boolean.{And, Not}
import eu.timepit.refined.collection.MinSize
import eu.timepit.refined.string.MatchesRegex
import joguin.earth.city.CityRepositoryOps
import scalaz.Free

package object joguin {
  type NonBlank = MinSize[W.`1`.T] And Not[MatchesRegex[W.`"""^\\s+$"""`.T]]
  type GenderLetter = MatchesRegex[W.`"""^[FMOfmo]$"""`.T]
  type Major = GreaterEqual[W.`18`.T]

  type Name = String Refined NonBlank
  type Country = String Refined NonBlank
  type Age = Int Refined Major
  type GenderCode = String Refined GenderLetter
  type DefensePower = Int Refined Positive

  type CityRepository[T] = Free[CityRepositoryOps,T]
}

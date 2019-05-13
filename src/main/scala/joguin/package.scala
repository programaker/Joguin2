import cats.Eq
import cats.implicits._
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.boolean.Not
import eu.timepit.refined.collection.MinSize
import eu.timepit.refined.string.MatchesRegex

package object joguin {
  type NonBlankString = MinSize[W.`1`.T] And Not[MatchesRegex[W.`"""^\\s+$"""`.T]]

  type NameR = NonBlankString
  type Name = String Refined NameR

  type LazyUnit[A, F[_]] = (=>A) => F[A]

  implicit def refinedEq[T: Eq, R]: Eq[Refined[T, R]] =
    (x: Refined[T, R], y: Refined[T, R]) => x.value === y.value
}

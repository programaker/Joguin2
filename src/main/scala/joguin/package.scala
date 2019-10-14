import cats.Eq
import cats.MonadError
import cats.implicits._
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.boolean.Not
import eu.timepit.refined.collection.MinSize
import eu.timepit.refined.refineV
import eu.timepit.refined.string.MatchesRegex

package object joguin {
  type NonBlankString = MinSize[W.`1`.T] And Not[MatchesRegex[W.`"""^\\s+$"""`.T]]

  type NameR = NonBlankString
  type Name = String Refined NameR

  type Recovery[F[_]] = MonadError[F, Throwable]

  /** I would like to use IndexedSeq instead of Vector, to be able to
   * easily replace it with other Seqs with fast apply and size, but
   * There is no Foldable instance for it - which provides the nice
   * get[A]: Long => Option[A] function.
   *
   * So, type alias to the rescue! */
  type IdxSeq[+A] = Vector[A]
  val IdxSeq: Vector.type = Vector

  implicit def refinedEq[T: Eq, R]: Eq[Refined[T, R]] =
    (x: Refined[T, R], y: Refined[T, R]) => x.value === y.value

  def parseName(name: String): Option[Name] =
    refineV[NameR](name).toOption
}

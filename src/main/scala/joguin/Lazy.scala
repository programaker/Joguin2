package joguin

import cats.Id
import cats.Monad
import zio.Task

/** Typeclass to lift a normal value into an Effect context lazily.
 *
 * Useful to store impure code inside a proper functional data type without
 * coupling with any specific IO implementation */
abstract class Lazy[F[_]] {
  def lift[A](a: => A): F[A]
}

object Lazy {
  def apply[F[_]](implicit instance: Lazy[F]): Lazy[F] = instance

  implicit val idLazy: Lazy[Id] = new Lazy[Id] {
    override def lift[A](a: => A): Id[A] = Monad[Id].pure(a)
  }

  implicit val zioLazy: Lazy[Task] = new Lazy[Task] {
    override def lift[A](a: => A): Task[A] = Task.effect(a)
  }
}

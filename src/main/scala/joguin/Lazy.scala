package joguin

import cats.Id
import cats.Monad
import cats.effect.IO

trait Lazy[F[_]] {
  def lift[A](a: => A): F[A]
}

object Lazy {
  def apply[F[_]](implicit instance: Lazy[F]): Lazy[F] = instance

  implicit val ioLazy: Lazy[IO] = new Lazy[IO] {
    override def lift[A](a: => A): IO[A] = IO(a)
  }

  implicit val idLazy: Lazy[Id] = new Lazy[Id] {
    override def lift[A](a: => A): Id[A] = Monad[Id].pure(a)
  }
}

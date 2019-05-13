package joguin

import cats.Id
import cats.effect.IO

trait LazyPure[F[_]] {
  def lazyPure[A](a: =>A): F[A]
}

object LazyPure {
  def apply[F[_]](implicit instance: LazyPure[F]): LazyPure[F] = instance

  implicit val IOLazyPure: LazyPure[IO] = new LazyPure[IO] {
    override def lazyPure[A](a: =>A): IO[A] = IO(a)
  }

  implicit val IdLazyPure: LazyPure[Id] = new LazyPure[Id] {
    override def lazyPure[A](a: =>A): Id[A] = a:Id[A]
  }
}

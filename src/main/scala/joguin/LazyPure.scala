package joguin

import cats.Id
import cats.effect.IO

trait LazyPure[F[_]] {
  def lazyPure[A](body: =>A): F[A]
}

object LazyPure {
  def apply[F[_]](implicit instance: LazyPure[F]): LazyPure[F] = instance

  implicit val IOLazyPure: LazyPure[IO] = new LazyPure[IO] {
    override def lazyPure[A](body: =>A): IO[A] = IO(body)
  }

  implicit val IdLazyPure: LazyPure[Id] = new LazyPure[Id] {
    override def lazyPure[A](body: =>A): Id[A] = body:Id[A]
  }
}

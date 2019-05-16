package joguin

import cats.Id
import cats.Monad
import cats.effect.IO

trait LazyEff[F[_]] {
  def wrap[A](a: => A): F[A]
}

object LazyEff {
  def apply[F[_]](implicit instance: LazyEff[F]): LazyEff[F] = instance

  implicit val ioLazyEff: LazyEff[IO] = new LazyEff[IO] {
    override def wrap[A](a: => A): IO[A] = IO(a)
  }

  implicit val idLazyEff: LazyEff[Id] = new LazyEff[Id] {
    override def wrap[A](a: => A): Id[A] = Monad[Id].pure(a)
  }
}

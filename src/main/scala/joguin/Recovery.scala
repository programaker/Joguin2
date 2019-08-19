package joguin

import cats.MonadError
import zio.Task

object Recovery {
  def apply[F[_]](implicit F: MonadError[F, Throwable]): Recovery[F] = MonadError[F, Throwable]

  implicit val zioRecovery: Recovery[Task] = new MonadError[Task, Throwable] {
    override def pure[A](x: A): Task[A] =
      Task.succeed(x)

    override def flatMap[A, B](fa: Task[A])(f: A => Task[B]): Task[B] =
      fa.flatMap(f)

    override def tailRecM[A, B](a: A)(f: A => Task[Either[A, B]]): Task[B] = f(a).flatMap {
      case Left(aValue)  => tailRecM(aValue)(f)
      case Right(bValue) => Task.effect(bValue)
    }

    override def raiseError[A](e: Throwable): Task[A] =
      Task.fail(e)

    override def handleErrorWith[A](fa: Task[A])(f: Throwable => Task[A]): Task[A] =
      fa.catchAll(f)
  }
}

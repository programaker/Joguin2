import cats.Eq
import cats.MonadError
import cats.implicits._
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.And
import eu.timepit.refined.boolean.Not
import eu.timepit.refined.collection.MinSize
import eu.timepit.refined.string.MatchesRegex
import zio.Task

package object joguin {
  type NonBlankString = MinSize[W.`1`.T] And Not[MatchesRegex[W.`"""^\\s+$"""`.T]]

  type NameR = NonBlankString
  type Name = String Refined NameR

  type Recovery[F[_]] = MonadError[F, Throwable]
  object Recovery {
    def apply[F[_]](implicit F: MonadError[F, Throwable]): Recovery[F] = MonadError[F, Throwable]
  }

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

  implicit def refinedEq[T: Eq, R]: Eq[Refined[T, R]] =
    (x: Refined[T, R], y: Refined[T, R]) => x.value === y.value
}

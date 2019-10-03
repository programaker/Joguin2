package joguin.playerinteraction.interaction

import cats.~>
import joguin.Lazy

import scala.io.StdIn.readLine

/** InteractionF root interpreter to any F that interacts with the player through the console */
final class InteractionInterpreter[F[_]: Lazy] extends (InteractionF ~> F) {
  override def apply[A](fa: InteractionF[A]): F[A] = fa match {
    case WriteMessage(message) => write(message)
    case ReadAnswer            => read()
  }

  private def write(message: String): F[Unit] = Lazy[F].lift(print(message))
  private def read(): F[String] = Lazy[F].lift(readLine())
}

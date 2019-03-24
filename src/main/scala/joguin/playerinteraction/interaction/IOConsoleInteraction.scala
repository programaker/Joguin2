package joguin.playerinteraction.interaction

import cats.effect.IO
import cats.~>

import scala.io.StdIn.readLine

/** InteractionOp interpreter for IO that interacts with the player through the console */
object IOConsoleInteraction extends (InteractionF ~> IO) {
  override def apply[A](fa: InteractionF[A]): IO[A] = fa match {
    case WriteMessage(message) => write(message)
    case ReadAnswer            => read()
  }

  private def write(message: String): IO[Unit] = IO(println(message))
  private def read(): IO[String] = IO(readLine())
}

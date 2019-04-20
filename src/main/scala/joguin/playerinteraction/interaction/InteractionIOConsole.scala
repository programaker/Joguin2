package joguin.playerinteraction.interaction

import cats.effect.IO
import cats.~>

import scala.io.StdIn.readLine

/** InteractionF root interpreter to IO that interacts with the player through the console */
object InteractionIOConsole extends (InteractionF ~> IO) {
  override def apply[A](fa: InteractionF[A]): IO[A] = fa match {
    case WriteMessage(message) => write(message)
    case ReadAnswer => read()
  }

  private def write(message: String): IO[Unit] = IO(print(message))
  private def read(): IO[String] = IO(readLine())
}

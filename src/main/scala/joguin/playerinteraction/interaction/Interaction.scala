package joguin.playerinteraction.interaction

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait InteractionF[A]
final case class WriteMessage(message: String) extends InteractionF[Unit]
case object ReadAnswer extends InteractionF[String]

final class InteractionOps[G[_]](implicit i: InjectK[InteractionF, G]) {
  def writeMessage(message: String): Free[G, Unit] =
    inject[InteractionF, G](WriteMessage(message))

  def readAnswer: Free[G, String] =
    inject[InteractionF, G](ReadAnswer)

  /** To reuse the following flow in all game steps:
   *
   * Ask something from player
   * -> Read response
   * -> Parse response
   * -> Validate parsed response
   * -> Tell player about the error
   * -> Retry until the player give a valid answer
   * */
  def ask[T](
    message: String,
    errorMessage: String,
    parseAnswer: String => Option[T]
  ): Free[G, T] = {
    val validatedAnswer = for {
      _ <- writeMessage(message)
      answer <- readAnswer
      parsedAnswer <- pure(parseAnswer(answer))
    } yield parsedAnswer

    validatedAnswer.flatMap {
      case Some(t) => pure(t)
      case None => ask(message, errorMessage, parseAnswer)
    }
  }
}
object InteractionOps {
  implicit def create[G[_]](implicit i: InjectK[InteractionF, G]): InteractionOps[G] =
    new InteractionOps[G]
}

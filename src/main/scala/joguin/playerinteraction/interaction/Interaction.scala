package joguin.playerinteraction.interaction

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed abstract class InteractionF[A] extends Product with Serializable
final case class WriteMessage(message: String) extends InteractionF[Unit]
case object ReadAnswer extends InteractionF[String]

final class InteractionOps[C[_]](implicit i: InjectK[InteractionF, C]) {
  def writeMessage(message: String): Free[C, Unit] =
    inject[InteractionF, C](WriteMessage(message))

  def readAnswer: Free[C, String] =
    inject[InteractionF, C](ReadAnswer)

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
  ): Free[C, T] = {
    val validatedAnswer = for {
      _            <- writeMessage(message)
      answer       <- readAnswer
      parsedAnswer <- pure(parseAnswer(answer))
    } yield parsedAnswer

    validatedAnswer.flatMap {
      case Some(t) => pure(t)
      case None    => writeMessage(errorMessage).flatMap(_ => ask(message, errorMessage, parseAnswer))
    }
  }
}

object InteractionOps {
  implicit def interactionOps[C[_]](implicit i: InjectK[InteractionF, C]): InteractionOps[C] =
    new InteractionOps[C]
}

package joguin.playerinteraction

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait InteractionOp[A]
case class WriteMessage(message: String) extends InteractionOp[Unit]
case object ReadAnswer extends InteractionOp[String]

class Interaction[F[_]](implicit I: InjectK[InteractionOp,F]) {
  def writeMessage(message: String): Free[F,Unit] =
    inject[InteractionOp,F](WriteMessage(message))

  def readAnswer: Free[F,String] =
    inject[InteractionOp,F](ReadAnswer)
}

object Interaction {
  implicit def create[F[_]](implicit I: InjectK[InteractionOp,F]): Interaction[F] = new Interaction

  /** To reuse the following flow in all game steps:
    *
    * Ask something from player
    * -> Read response
    * -> Parse response
    * -> Validate parsed response
    * -> Tell player about the error
    * -> Retry until the player give a valid answer
    * */
  def ask[F[_],T](
      message: String,
      errorMessage: String,
      parseAnswer: String => Option[T],
      validAnswer: T => Boolean)(implicit I: Interaction[F]): Free[F,T] = {

    import I._

    val validatedAnswer = for {
      _ <- writeMessage(message)
      answer <- readAnswer
      parsedAnswer <- pure(parseAnswer(answer))
    } yield {
      parsedAnswer.filter(validAnswer)
    }

    validatedAnswer.flatMap {
      case Some(validT) => pure[F,T](validT)
      case None => ask[F,T](message, errorMessage, parseAnswer, validAnswer)
    }
  }
}

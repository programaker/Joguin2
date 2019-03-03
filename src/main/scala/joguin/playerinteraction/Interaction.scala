package joguin.playerinteraction

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait InteractionOp[A]
case class WriteMessage(message: String) extends InteractionOp[Unit]
case object ReadAnswer extends InteractionOp[String]
case class ParseAnswer[T](value: String) extends InteractionOp[Either[String,T]]
case class ValidateAnswer[T](parsed: T) extends InteractionOp[Either[String,T]]

class Interaction[F[_]](implicit I: InjectK[InteractionOp,F]) {
  def writeMessage(message: String): Free[F,Unit] =
    inject[InteractionOp,F](WriteMessage(message))

  def readAnswer: Free[F,String] =
    inject[InteractionOp,F](ReadAnswer)

  def parseAnswer[T](value: String): Free[F,Either[String,T]] =
    inject[InteractionOp,F](ParseAnswer(value))

  def validateAnswer[T](parsed: T): Free[F,Either[String,T]] =
    inject[InteractionOp,F](ValidateAnswer(parsed))
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
  def ask[F[_],T](message: String, errorMessage: String)(implicit I: Interaction[F]): Free[F,T] = {
    import I._

    val parsedAnswer = for {
      _ <- writeMessage(message)
      answer <- readAnswer
      parsedAnswer <- parseAnswer[T](answer)
    } yield parsedAnswer

    parsedAnswer
      .flatMap {
        case Right(parsedT) =>
          validateAnswer[T](parsedT)

        case left @ Left(_) =>
          pure[F,Either[String,T]](left)
      }
      .flatMap {
        case Right(validT) =>
          pure[F,T](validT)

        case Left(_) =>
          writeMessage(errorMessage).flatMap(_ => ask[F,T](message, errorMessage))
      }
  }
}

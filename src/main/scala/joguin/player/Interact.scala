package joguin.player

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait InteractOp[A]
case class WriteMessage(message: String) extends InteractOp[Unit]
case object ReadAnswer extends InteractOp[String]
case class ParseAnswer[T](value: String) extends InteractOp[Either[String,T]]
case class ValidateAnswer[T](parsed: T) extends InteractOp[Either[String,T]]
//case class Retry[T]() extends InteractOp[T]

class Interact[F[_]](implicit I: InjectK[InteractOp,F]) {
  def writeMessage(message: String): Free[F,Unit] =
    inject[InteractOp,F](WriteMessage(message))

  def readAnswer: Free[F,String] =
    inject[InteractOp,F](ReadAnswer)

  def parseAnswer[T](value: String): Free[F,Either[String,T]] =
    inject[InteractOp,F](ParseAnswer(value))

  def validateAnswer[T](parsed: T): Free[F,Either[String,T]] =
    inject[InteractOp,F](ValidateAnswer(parsed))

  /*def retry[T]: Free[F,T] =
    inject[InteractOp,F](Retry())*/
}

object Interact {
  implicit def create[F[_]](implicit I: InjectK[InteractOp,F]): Interact[F] = new Interact

  /** To reuse the following flow in all game steps:
    *
    * Ask something from player
    * -> Read response
    * -> Parse response
    * -> Validate parsed response
    * -> Tell player about the error
    * -> Retry until the player give a valid answer
    * */
  def ask[F[_],T](message: String)(implicit I: Interact[F]): Free[F,T] = {
    import I._

    val parsedAnswer = for {
      _ <- writeMessage(message)
      answer <- readAnswer
      parsedAnswer <- parseAnswer[T](answer)
    } yield parsedAnswer

    val validatedAnswer = parsedAnswer.flatMap {
      case Right(t) => validateAnswer[T](t)
      case left @ Left(_) => pure[F,Either[String,T]](left)
    }

    validatedAnswer.flatMap {
      case Right(t) => pure[F,T](t)
      case Left(error) => writeMessage(error).flatMap(_ => ask[F,T](message))
    }
  }
}

package joguin.player

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait InteractOp[A]
case class WriteMessage(message: String) extends InteractOp[Unit]
case object ReadAnswer extends InteractOp[String]
case class ParseAnswer[T](value: String) extends InteractOp[Either[String,T]]
case class ValidateAnswer[T](parsed: T) extends InteractOp[Either[String,T]]

class Interact[F[_]](implicit I: InjectK[InteractOp,F]) {
  def writeMessage(message: String): Free[F,Unit] =
    inject[InteractOp,F](WriteMessage(message))

  def readAnswer: Free[F,String] =
    inject[InteractOp,F](ReadAnswer)

  def parseAnswer[T](value: String): Free[F,Either[String,T]] =
    inject[InteractOp,F](ParseAnswer(value))

  def validateAnswer[T](parsed: T): Free[F,Either[String,T]] =
    inject[InteractOp,F](ValidateAnswer(parsed))
}

object Interact {
  implicit def create[F[_]](implicit I: InjectK[InteractOp,F]): Interact[F] = new Interact

  def ask[F[_],T](message: String)(implicit I: Interact[F]): Free[F,T] = {
    import I._

    val parsedAnswer = for {
      _ <- writeMessage(message)
      answer <- readAnswer
      parsedAnswer <- parseAnswer[T](answer)
    } yield parsedAnswer

    parsedAnswer.flatMap {
      case Right(b) => pure(b)
      case Left(error) => writeMessage(error).flatMap(_ => ask(message))
    }
  }
}

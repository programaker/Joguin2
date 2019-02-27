package joguin.player

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait InteractF[A]
case class WriteMessage(message: String) extends InteractF[Unit]
case object ReadAnswer extends InteractF[String]
case class ParseAnswer[T](value: String) extends InteractF[Either[String,T]]
case class ValidateAnswer[T](parsed: T) extends InteractF[Either[String,T]]

class Interact[F[_]](implicit I: InjectK[InteractF,F]) {
  def writeMessage(message: String): Free[F,Unit] =
    inject[InteractF,F](WriteMessage(message))

  def readAnswer: Free[F,String] =
    inject[InteractF,F](ReadAnswer)

  def parseAnswer[T](value: String): Free[F,Either[String,T]] =
    inject[InteractF,F](ParseAnswer(value))

  def validateAnswer[T](parsed: T): Free[F,Either[String,T]] =
    inject[InteractF,F](ValidateAnswer(parsed))
}

object Interact {
  implicit def create[F[_]](implicit I: InjectK[InteractF,F]): Interact[F] = new Interact

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

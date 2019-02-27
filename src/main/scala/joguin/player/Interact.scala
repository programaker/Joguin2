package joguin.player

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait InteractF[A]
case class WriteMessage(message: String) extends InteractF[Unit]
case object ReadAnswer extends InteractF[String]
case class ParseAnswer[B](value: String) extends InteractF[Either[String,B]]
case class ValidateAnswer[B](parsed: B) extends InteractF[Either[String,B]]

class Interact[C[_]](implicit I: InjectK[InteractF,C]) {
  def writeMessage(message: String): Free[C,Unit] =
    inject[InteractF,C](WriteMessage(message))

  def readAnswer: Free[C,String] =
    inject[InteractF,C](ReadAnswer)

  def parseAnswer[B](value: String): Free[C,Either[String,B]] =
    inject[InteractF,C](ParseAnswer(value))

  def validateAnswer[B](parsed: B): Free[C,Either[String,B]] =
    inject[InteractF,C](ValidateAnswer(parsed))
}

object Interact {
  implicit def create[C[_]](implicit I: InjectK[InteractF,C]): Interact[C] = new Interact

  def ask[C[_],B](message: String)(implicit I: Interact[C]): Free[C,B] = {
    import I._

    val parsedAnswer = for {
      _ <- writeMessage(message)
      answer <- readAnswer
      parsedAnswer <- parseAnswer[B](answer)
    } yield parsedAnswer

    parsedAnswer.flatMap {
      case Right(b) => pure(b)
      case Left(error) => writeMessage(error).flatMap(_ => ask(message))
    }
  }
}

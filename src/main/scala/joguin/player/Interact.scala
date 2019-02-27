package joguin.player

import scalaz.Free._
import scalaz.{Free, Inject}

sealed trait InteractF[A]
case class WriteMessage(message: String) extends InteractF[Unit]
case object ReadAnswer extends InteractF[String]
case class ParseAnswer[B](value: String) extends InteractF[Either[String,B]]
case class ValidateAnswer[B](parsed: B) extends InteractF[Either[String,B]]

class Interact[C[_]](implicit i: Inject[InteractF,C]) {
  def writeMessage(message: String): Free[C,Unit] =
    liftF(i.inj(WriteMessage(message)))

  def readAnswer: Free[C,String] =
    liftF(i.inj(ReadAnswer))

  def parseAnswer[B](value: String): Free[C,Either[String,B]] =
    liftF[C, Either[String,B]](i.inj(ParseAnswer(value)))

  def validateAnswer[B](parsed: B): Free[C,Either[String,B]] =
    liftF[C, Either[String,B]](i.inj(ValidateAnswer(parsed)))
}

object Interact {
  implicit def create[C[_]](implicit i: Inject[InteractF,C]): Interact[C] = new Interact

  def ask[C[_],A](message: String)(implicit i: Interact[C]): Free[C,A] = {
    val pa = for {
      _ <- i.writeMessage(message)
      answer <- i.readAnswer
      parsedAnswer <- i.parseAnswer[A](answer)
    } yield parsedAnswer

    pa.flatMap {
      case Right(b) => pure(b)
      case Left(error) => i.writeMessage(error).flatMap(_ => ask(message))
    }
  }
}

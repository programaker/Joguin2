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
    liftF(i.inj(ParseAnswer(value)))

  def validateAnswer[B](parsed: B): Free[C,Either[String,B]] =
    liftF(i.inj(ValidateAnswer(parsed)))
}

object Interact {
  implicit def create[C[_]](implicit i: Inject[InteractF,C]): Interact[C] = new Interact

  def ask[C[_],B](message: String)(implicit i: Interact[C]): Free[C,B] = {
    import i._

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

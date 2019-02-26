package joguin.player

import scalaz.Free._

sealed trait InteractF[A]
case class WriteMessage(message: String) extends InteractF[Unit]
case object ReadAnswer extends InteractF[String]
case class ParseAnswer[B](value: String) extends InteractF[Either[String,B]]
case class ValidateAnswer[B](parsed: B) extends InteractF[Either[String,B]]

object Interact {
  def writeMessage(message: String): Interact[Unit] = liftF(WriteMessage(message))
  def readAnswer: Interact[String] = liftF(ReadAnswer)
  def parseAnswer[B](value: String): Interact[Either[String,B]] = liftF[InteractF, Either[String,B]](ParseAnswer(value))
  def validateAnswer[B](parsed: B): Interact[Either[String,B]] = liftF[InteractF, Either[String,B]](ValidateAnswer(parsed))

  def ask[B](message: String): Interact[B] = {
    val pa = for {
      _ <- writeMessage(message)
      answer <- readAnswer
      parsedAnswer <- parseAnswer[B](answer)
    } yield parsedAnswer

    pa.flatMap {
      case Right(b) => pure(b)
      case Left(error) => writeMessage(error).flatMap(_ => ask[B](message))
    }
  }
}

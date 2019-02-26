package joguin.player

import scalaz.Free._

sealed trait InteractF[A]
case class WriteMessage(message: String) extends InteractF[Unit]
case object ReadAnswer extends InteractF[String]

/*trait ParseAnswerF[A]
trait ValidateAnswerF[A]*/

object Interact {
  /*def writeMessage(message: String): Interact[Unit] = liftF(WriteMessage(message))
  def readAnswer: Interact[String] = liftF(ReadAnswer)
  def parseAnswer[A](answer: String, pa: ParseAnswerE[A]): ParseAnswer[A] = liftF(pa)
  def validateAnswer[A](parsedAnswer: A, va: ValidateAnswerE[A]): ValidateAnswer[A] = liftF(va)

  def ask[A](message: String, pa: ParseAnswerE[A], va: ValidateAnswerE[A]): Interact[A] = for {
    _ <- writeMessage(message)
    answer <- readAnswer
    parsedAnswer <- parseAnswer(answer, pa)
    validatedAnswer <- validateAnswer(parsedAnswer, va)
  } yield {
    validatedAnswer
  }*/
}

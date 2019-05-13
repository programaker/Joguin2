package joguin.testutil.interpreter

final case class MessageAnswer(message: String, answer: String)

object MessageAnswer {
  type Track = Map[Int, MessageAnswer]
}

package joguin.testutil.interpreter

final case class WriteMessageTrack(
  currentMessage: String,
  currentIndex: Int,
  indexedMessages: Map[Int, String],
  answersByQuestion: Map[String, List[String]]
)

object WriteMessageTrack {
  def of(answersByQuestion: Map[String, List[String]]): WriteMessageTrack =
    WriteMessageTrack(
      currentMessage = "",
      currentIndex = 0,
      indexedMessages = Map.empty,
      answersByQuestion = answersByQuestion
    )
}

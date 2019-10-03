package joguin.testutil.interpreter

final case class WriteMessageTrack(
  currentMessage: String,
  currentIndex: Int,
  indexedMessages: Map[Int, String],
  answersByQuestion: Map[String, List[String]]
)

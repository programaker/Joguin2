package joguin.playerinteraction.message

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait MessagesF[A]

final case class GetMessage[T <: MessageSource](
  source: LocalizedMessageSource[T],
  key: T#Key
) extends MessagesF[String]

final case class GetMessageFmt[T <: MessageSource](
  source: LocalizedMessageSource[T],
  key: T#Key,
  args: List[String]
) extends MessagesF[String]


final class MessagesOps[C[_]](implicit i: InjectK[MessagesF, C]) {
  def getMessage[T <: MessageSource]
      (source: LocalizedMessageSource[T])
      (key: T#Key): Free[C, String] = {

    inject[MessagesF, C](GetMessage(source, key))
  }

  def getMessageFmt[T <: MessageSource]
      (source: LocalizedMessageSource[T])
      (key: T#Key, args: List[String]): Free[C, String] = {

    inject[MessagesF, C](GetMessageFmt(source, key, args))
  }
}

object MessagesOps {
  implicit def messagesOps[C[_]](implicit i: InjectK[MessagesF, C]): MessagesOps[C] = new MessagesOps[C]
}

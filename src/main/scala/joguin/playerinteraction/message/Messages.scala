package joguin.playerinteraction.message

import cats.InjectK
import cats.free.Free
import cats.free.Free._
import joguin.playerinteraction.message.MessagesF.GetLocalizedMessageSource
import joguin.playerinteraction.message.MessagesF.GetMessage
import joguin.playerinteraction.message.MessagesF.GetMessageFmt

sealed abstract class MessagesF[A] extends Product with Serializable

object MessagesF {
  final case class GetLocalizedMessageSource[T <: MessageSource](source: T) extends MessagesF[LocalizedMessageSource[T]]

  final case class GetMessage[T <: MessageSource](
    source: LocalizedMessageSource[T],
    key: T#Key
  ) extends MessagesF[String]

  final case class GetMessageFmt[T <: MessageSource](
    source: LocalizedMessageSource[T],
    key: T#Key,
    args: List[String]
  ) extends MessagesF[String]
}

final class MessagesOps[C[_]](implicit i: InjectK[MessagesF, C]) {
  def getLocalizedMessageSource[T <: MessageSource](source: T): Free[C, LocalizedMessageSource[T]] =
    inject[MessagesF, C](GetLocalizedMessageSource(source))

  def getMessage[T <: MessageSource](source: LocalizedMessageSource[T])(key: T#Key): Free[C, String] =
    inject[MessagesF, C](GetMessage(source, key))

  def getMessageFmt[T <: MessageSource](
    source: LocalizedMessageSource[T]
  )(key: T#Key, args: List[String]): Free[C, String] =
    inject[MessagesF, C](GetMessageFmt(source, key, args))
}

object MessagesOps {
  implicit def messagesOps[C[_]](implicit i: InjectK[MessagesF, C]): MessagesOps[C] = new MessagesOps[C]
}

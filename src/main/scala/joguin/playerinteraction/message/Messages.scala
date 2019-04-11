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


final class MessagesOps[G[_]](implicit i: InjectK[MessagesF, G]) {
  def getMessage[T <: MessageSource]
      (source: LocalizedMessageSource[T])
      (key: T#Key): Free[G, String] = {

    inject[MessagesF, G](GetMessage(source, key))
  }

  def getMessageFmt[T <: MessageSource]
      (source: LocalizedMessageSource[T])
      (key: T#Key, args: List[String]): Free[G, String] = {

    inject[MessagesF, G](GetMessageFmt(source, key, args))
  }
}
object MessagesOps {
  implicit def create[G[_]](implicit i: InjectK[MessagesF, G]): MessagesOps[G] = new MessagesOps[G]
}

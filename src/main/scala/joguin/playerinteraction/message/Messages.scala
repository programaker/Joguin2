package joguin.playerinteraction.message

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed trait MessagesF[A]
final case class GetMessage(source: LocalizedMessageSource, key: String, args: Seq[String]) extends MessagesF[String]

final class MessagesOps[G[_]](implicit I: InjectK[MessagesF, G]) {
  def getMessage(source: LocalizedMessageSource, key: String, args: String*): Free[G, String] =
    inject[MessagesF, G](GetMessage(source, key, args))
}
object MessagesOps {
  implicit def create[G[_]](implicit I: InjectK[MessagesF, G]): MessagesOps[G] = new MessagesOps[G]
}

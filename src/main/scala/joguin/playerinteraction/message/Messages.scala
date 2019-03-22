package joguin.playerinteraction.message

import java.util.Locale

import cats.InjectK
import cats.free.Free
import cats.free.Free._


sealed trait MessageSource
case object CreateCharacterMessageSource extends MessageSource
case object ExploreMessageSource extends MessageSource
case object QuitMessageSource extends MessageSource
case object ShowIntroMessageSource extends MessageSource
case object FightMessageSource extends MessageSource
case object SaveGameMessageSource extends MessageSource


final case class LocalizedMessageSource(source: MessageSource, locale: Locale)

object LocalizedMessageSource {
  //This opens the possibility of internationalization,
  //but it only knows english for now
  def of(source: MessageSource): LocalizedMessageSource = LocalizedMessageSource(source, Locale.US)
}


sealed trait MessagesOp[A]
final case class Message(source: LocalizedMessageSource, key: String, args: Seq[String]) extends MessagesOp[String]


final class Messages[F[_]](implicit I: InjectK[MessagesOp,F]) {
  def message(source: LocalizedMessageSource, key: String, args: String*): Free[F,String] =
    inject[MessagesOp,F](Message(source, key, args))
}

object Messages {
  implicit def create[F[_]](implicit I: InjectK[MessagesOp,F]): Messages[F] = new Messages[F]
}

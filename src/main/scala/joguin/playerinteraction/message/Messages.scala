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

//This opens the possibility of internationalization,
//but it only knows english for now
case class LocalizedMessageSource(source: MessageSource, locale: Locale = Locale.US)

sealed trait MessagesOp[A]
case class Read(source: LocalizedMessageSource, key: String, args: Seq[String]) extends MessagesOp[String]

class Messages[F[_]](implicit I: InjectK[MessagesOp,F]) {
  def read(source: LocalizedMessageSource, key: String, args: String*): Free[F,String] =
    inject[MessagesOp,F](Read(source, key, args))
}

object Messages {
  implicit def create[F[_]](implicit I: InjectK[MessagesOp,F]): Messages[F] = new Messages[F]
}

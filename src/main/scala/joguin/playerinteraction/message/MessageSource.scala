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

sealed trait MessageSourceF[A]
final case class GetLocalizedMessageSource(source: MessageSource) extends MessageSourceF[LocalizedMessageSource]

final class MessageSourceOps[G[_]](implicit I: InjectK[MessageSourceF,G]) {
  def getLocalizedMessageSource(source: MessageSource): Free[G,LocalizedMessageSource] =
    inject[MessageSourceF,G](GetLocalizedMessageSource(source))
}
object MessageSourceOps {
  implicit def create[G[_]](implicit I: InjectK[MessageSourceF,G]): MessageSourceOps[G] =
    new MessageSourceOps[G]
}
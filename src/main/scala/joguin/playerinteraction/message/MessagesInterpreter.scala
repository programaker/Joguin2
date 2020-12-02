package joguin.playerinteraction.message

import java.text.MessageFormat.format
import java.util.Locale
import java.util.ResourceBundle
import java.util.ResourceBundle.getBundle

import cats.Monad
import cats.~>
import cats.syntax.functor._
import cats.syntax.flatMap._
import joguin.Lazy
import joguin.playerinteraction.message.MessageSource._
import joguin.playerinteraction.message.MessagesF._

/** MessagesF root interpreter to any F that uses ResourceBundle to read messages from app resources */
final class MessagesInterpreter[F[_]: Monad: Lazy] extends (MessagesF ~> F) {
  override def apply[A](fa: MessagesF[A]): F[A] = fa match {
    case GetLocalizedMessageSource(source) => getLocalizedMessageSource(source)
    case GetMessage(source, key)           => message(source, key, Nil)
    case GetMessageFmt(source, key, args)  => message(source, key, args)
  }

  private def getLocalizedMessageSource[T <: MessageSource](source: T): F[LocalizedMessageSource[T]] =
    //This opens the possibility of internationalization,
    //but it only knows english for now
    Monad[F].pure(LocalizedMessageSource(source, Locale.US))

  private def message[T <: MessageSource](
    source: LocalizedMessageSource[T],
    key: T#Key,
    args: List[String]
  ): F[String] =
    Monad[F]
      .pure(source)
      .map(resourceBundleParams)
      .flatMap(resourceBundle)
      .flatMap(rb => Lazy[F].lift(rb.getString(keyName(key))))
      .map(format(_, args: _*))

  private def resourceBundle(params: (String, Locale)): F[ResourceBundle] =
    Monad[F].pure(params).flatMap {
      case (name, locale) =>
        Lazy[F].lift(getBundle(name, locale))
    }

  private def resourceBundleParams[T <: MessageSource](lms: LocalizedMessageSource[T]): (String, Locale) =
    (sourceName(lms.source), lms.locale)

  private def sourceName(src: MessageSource): String = src match {
    case CreateCharacterMessageSource => "CreateCharacterMessageSource"
    case ExploreMessageSource         => "ExploreMessageSource"
    case QuitMessageSource            => "QuitMessageSource"
    case ShowIntroMessageSource       => "ShowIntroMessageSource"
    case FightMessageSource           => "FightMessageSource"
    case SaveGameMessageSource        => "SaveGameMessageSource"
  }

  private def keyName[T <: MessageSource](key: T#Key): String =
    //too lazy to map all keys to String
    s"${key.getClass.getSimpleName.replaceAll("\\$", "")}"
}

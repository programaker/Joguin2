package joguin.playerinteraction.message

import java.text.MessageFormat.format
import java.util.ResourceBundle.getBundle
import java.util.{Locale, ResourceBundle}

import cats.effect.IO
import cats.~>

/** MessagesF root interpreter to IO that uses ResourceBundle to read messages from app resources */
object MessagesIOInterpreter extends (MessagesF ~> IO) {
  override def apply[A](fa: MessagesF[A]): IO[A] = fa match {
    case GetMessage(source, key) => message(source, key, Nil)
    case GetMessageFmt(source, key, args) => message(source, key, args)
  }

  private def message[T <: MessageSource](
    source: LocalizedMessageSource[T], 
    key: T#Key, 
    args: List[String]
  ): IO[String] = {
   
    IO.pure(source)
      .map(resourceBundleParams)
      .flatMap(resourceBundle)
      .flatMap(rb => IO(rb.getString(keyName(key))))
      .map(format(_, args: _*))
  }    

  private def resourceBundle(params: (String, Locale)): IO[ResourceBundle] =
    IO.pure(params).flatMap { case (name, locale) => IO(getBundle(name, locale)) }

  private def resourceBundleParams[T <: MessageSource](lms: LocalizedMessageSource[T]): (String, Locale) =
    (sourceName(lms.source), lms.locale)

  private def sourceName(src: MessageSource): String = src match {
    case CreateCharacterMessageSource => "CreateCharacterMessageSource"
    case ExploreMessageSource => "ExploreMessageSource"
    case QuitMessageSource => "QuitMessageSource"
    case ShowIntroMessageSource => "ShowIntroMessageSource"
    case FightMessageSource => "FightMessageSource"
    case SaveGameMessageSource => "SaveGameMessageSource"
  }

  private def keyName[T <: MessageSource](key: T#Key): String =
    //too lazy to map all keys to String
    s"${key.getClass.getSimpleName.replaceAll("\\$", "")}"
}

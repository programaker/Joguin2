package joguin.playerinteraction.message

import java.text.MessageFormat.format
import java.util.ResourceBundle.getBundle
import java.util.{Locale, ResourceBundle}

import cats.effect.IO
import cats.~>

/** MessagesOp interpreter for IO that uses ResourceBundle to read messages from app resources */
object IOResourceBundleMessages extends (MessagesF ~> IO) {
  override def apply[A](fa: MessagesF[A]): IO[A] = fa match {
    case GetMessage(source, key) => message(source, key, Nil)
    case GetMessageFmt(source, key, args) => message(source, key, args)
  }

  private def message[T <: MessageSource](source: LocalizedMessageSource[T], key: T#Key, args: List[String]): IO[String] =
    IO.pure(source)
      .map(resourceBundleParams)
      .flatMap(resourceBundle)
      .flatMap(rb => IO(rb.getString(key)))
      .map(format(_, args: _*))

  private def resourceBundle(params: (String, Locale)): IO[ResourceBundle] =
    IO.pure(params).flatMap { case (name, locale) => IO(getBundle(name, locale)) }

  private def resourceBundleParams[T <: MessageSource](localizedSource: LocalizedMessageSource[T]): (String, Locale) =
    (bundleNamesBySource.getOrElse(localizedSource.source, "unknown"), localizedSource.locale)

  private val bundleNamesBySource = Map[MessageSource, String](
    CreateCharacterMessageSource -> "CreateCharacterMessages",
    ExploreMessageSource -> "ExploreMessages",
    QuitMessageSource -> "QuitMessages",
    ShowIntroMessageSource -> "ShowIntroMessages",
    FightMessageSource -> "FightMessages",
    SaveGameMessageSource -> "SaveGameMessages"
  )
}

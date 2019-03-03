package joguin.playerinteraction.message

import java.text.MessageFormat.format
import java.util.ResourceBundle.getBundle
import java.util.{Locale, ResourceBundle}

import cats.effect.IO
import cats.~>

/** MessagesOp interpreter for IO that uses ResourceBundle to read messages from app resources */
object IOResourceBundleMessages extends (MessagesOp ~> IO) {
  private val bundleNamesBySource = Map[MessageSource,String](
    CreateCharacterMessageSource -> "CreateCharacterMessages",
    ExploreMessageSource -> "ExploreMessages",
    QuitMessageSource -> "QuitMessages",
    ShowIntroMessageSource -> "ShowIntroMessages",
    FightMessageSource -> "FightMessages",
    SaveGameMessageSource -> "SaveGameMessages"
  )

  override def apply[A](op: MessagesOp[A]): IO[A] = op match {
    case Read(source, key, args) => read(source, key, args)
  }

  private def read(source: LocalizedMessageSource, key: String, args: Seq[String]): IO[String] =
    IO.pure(source)
      .map(resourceBundleParams)
      .flatMap(resourceBundle)
      .flatMap(rb => IO(rb.getString(key)))
      .map(format(_, args:_*))

  private def resourceBundle(params: (String,Locale)): IO[ResourceBundle] =
    IO.pure(params).flatMap{ case (name, locale) => IO(getBundle(name, locale)) }

  private def resourceBundleParams(localizedSource: LocalizedMessageSource): (String,Locale) =
    (bundleNamesBySource.getOrElse(localizedSource.source, "unknown"), localizedSource.locale)
}

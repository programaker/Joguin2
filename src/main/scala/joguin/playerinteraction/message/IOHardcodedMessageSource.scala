package joguin.playerinteraction.message

import java.util.Locale

import cats.effect.IO
import cats.~>

object IOHardcodedMessageSource extends (MessageSourceF ~> IO) {
  override def apply[A](fa: MessageSourceF[A]): IO[A] = fa match {
    case GetLocalizedMessageSource(source) => getLocalizedMessageSource(source)
  }

  private def getLocalizedMessageSource[T <: MessageSource](source: T): IO[LocalizedMessageSource[T]] =
    //This opens the possibility of internationalization,
    //but it only knows english for now
    IO.pure(LocalizedMessageSource(source, Locale.US))
}

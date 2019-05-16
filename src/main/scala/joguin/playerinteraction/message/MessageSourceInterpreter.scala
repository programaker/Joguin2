package joguin.playerinteraction.message

import java.util.Locale

import cats.Monad
import cats.~>

/** MessageSourceF root interpreter to any Monad that uses a hardcoded LocalizedMessageSource.
 * Having no restriction about the target Monad, it can be used for both production and test */
final class MessageSourceInterpreter[F[_]: Monad] extends (MessageSourceF ~> F) {
  override def apply[A](fa: MessageSourceF[A]): F[A] = fa match {
    case GetLocalizedMessageSource(source) => getLocalizedMessageSource(source)
  }

  private def getLocalizedMessageSource[T <: MessageSource](source: T): F[LocalizedMessageSource[T]] =
    //This opens the possibility of internationalization,
    //but it only knows english for now
    Monad[F].pure(LocalizedMessageSource(source, Locale.US))
}

object MessageSourceInterpreter {
  def apply[F[_]: Monad]: MessageSourceInterpreter[F] = new MessageSourceInterpreter[F]
}

package joguin.playerinteraction.message

import java.util.Locale

import cats.{Id, ~>}

object IdHardcodedMessageSource extends (MessageSourceF ~> Id) {
  override def apply[A](fa: MessageSourceF[A]): Id[A] = fa match {
    //This opens the possibility of internationalization,
    //but it only knows english for now
    case GetLocalizedMessageSource(source) => LocalizedMessageSource(source, Locale.US)
  }
}

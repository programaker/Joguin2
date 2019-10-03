package joguin.playerinteraction

import cats.free.Free
import cats.free.Free.pure

package object interaction {

  /** To reuse the following flow in all game steps:
   *
   * Ask something from player
   * -> Read response
   * -> Parse response
   * -> Validate parsed response
   * -> Tell player about the error
   * -> Retry until the player give a valid answer
   * */
  def ask[F[_], T](
    message: String,
    errorMessage: String,
    parseAnswer: String => Option[T]
  )(implicit i: InteractionOps[F]): Free[F, T] = {
    import i._

    val validatedAnswer = for {
      _            <- writeMessage(message)
      answer       <- readAnswer
      parsedAnswer <- pure(parseAnswer(answer))
    } yield parsedAnswer

    validatedAnswer.flatMap {
      case Some(t) => pure(t)
      case None    => writeMessage(errorMessage).flatMap(_ => ask(message, errorMessage, parseAnswer))
    }
  }

}

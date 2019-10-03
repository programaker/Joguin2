package joguin.testutil.interpreter

import cats.Monad
import cats.~>
import joguin.playerinteraction.wait.WaitF
import joguin.playerinteraction.wait.WaitFor

/** WaitF root interpreter to any F that does nothing. For test purposes only */
final class WaitInterpreter[F[_]: Monad] extends (WaitF ~> F) {
  override def apply[A](fa: WaitF[A]): F[A] = fa match {
    case WaitFor(_) => Monad[F].pure(())
  }
}

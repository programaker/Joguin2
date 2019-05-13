package joguin.testutil.interpreter

import cats.Id
import cats.~>
import joguin.playerinteraction.wait.WaitF
import joguin.playerinteraction.wait.WaitFor

/** WaitF root interpreter to Id that does nothing. For test purposes only */
object WaitIdInterpreter extends (WaitF ~> Id) {
  override def apply[A](fa: WaitF[A]): Id[A] = fa match {
    case WaitFor(_) => ()
  }
}

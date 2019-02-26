package joguin

import scalaz.Free

package object player {
  type Interact[A] = Free[InteractF,A]
}

package joguin

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive

package object alien {
  type PowerR = Positive
  type Power = Int Refined PowerR
}

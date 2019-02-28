package joguin

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive

package object alien {
  type Power = Int Refined Positive
}

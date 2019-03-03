package joguin.game

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.{NonNegative, Positive}

package object progress {
  type Experience = Int Refined NonNegative
  type Count = Int Refined NonNegative
  type Index = Int Refined Positive
}

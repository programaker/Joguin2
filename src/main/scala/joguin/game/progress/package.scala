package joguin.game

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.NonNegative
import eu.timepit.refined.numeric.Positive

package object progress {
  type ExperienceR = NonNegative
  type Experience = Int Refined ExperienceR

  type CountR = NonNegative
  type Count = Int Refined CountR

  type IndexR = Positive
  type Index = Int Refined IndexR
}

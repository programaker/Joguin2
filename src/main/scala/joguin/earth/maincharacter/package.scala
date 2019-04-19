package joguin.earth

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.GreaterEqual
import eu.timepit.refined.string.MatchesRegex

package object maincharacter {
  type AgeR = GreaterEqual[W.`18`.T]
  type Age = Int Refined AgeR

  type GenderR = MatchesRegex[W.`"""^[FMOfmo]$"""`.T]
  type GenderCode = String Refined GenderR
}

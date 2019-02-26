package joguin.earth

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.GreaterEqual
import eu.timepit.refined.string.MatchesRegex

package object maincharacter {
  type GenderSymbol = MatchesRegex[W.`"""^[FMOfmo]$"""`.T]
  type Major = GreaterEqual[W.`18`.T]
  type Age = Int Refined Major
  type GenderCode = String Refined GenderSymbol
}

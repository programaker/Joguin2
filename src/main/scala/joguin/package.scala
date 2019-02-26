import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.{And, Not}
import eu.timepit.refined.collection.MinSize
import eu.timepit.refined.string.MatchesRegex

package object joguin {
  type NonBlankString = MinSize[W.`1`.T] And Not[MatchesRegex[W.`"""^\\s+$"""`.T]]
  type Name = String Refined NonBlankString
}

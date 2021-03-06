package joguin.earth

import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.GreaterEqual
import eu.timepit.refined.numeric.NonNegative
import eu.timepit.refined.refineV
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.string.ValidInt
import joguin.earth.maincharacter.Gender._

package object maincharacter {
  type AgeR = GreaterEqual[18]
  type Age = Int Refined AgeR

  type GenderR = MatchesRegex["^[FMOfmo]$"]
  type GenderCode = String Refined GenderR

  type ExperienceR = NonNegative
  type Experience = Int Refined ExperienceR

  def parseAge(age: String): Option[Age] =
    refineV[ValidInt](age)
      .map(_.value.toInt)
      .flatMap(refineV[AgeR](_))
      .toOption

  def parseGender(code: String): Option[Gender] =
    code.toLowerCase match {
      case Female.code => Some(Female)
      case Male.code   => Some(Male)
      case Other.code  => Some(Other)
      case _           => None
    }
}

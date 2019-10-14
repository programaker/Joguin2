package joguin.testutil.generators

import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.Name
import joguin.NameR
import org.scalacheck.Gen

package object name {
  def genName: Gen[Name] =
    Gen.alphaStr
      .map(refineV[NameR](_))
      .map(_.getOrElse("Fallback Name"))

  def genInvalidName: Gen[String] = Gen.oneOf("", " ", "   ")
}

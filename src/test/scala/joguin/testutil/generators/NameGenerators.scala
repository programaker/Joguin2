package joguin.testutil.generators

import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.Name
import joguin.NameR
import org.scalacheck.Gen

object NameGenerators {
  def genName: Gen[Name] =
    Gen.alphaStr
      .map(refineV[NameR](_))
      .map(_.getOrElse("Fallback Name": Name))

  def genInvalidName: Gen[String] =
    Gen.oneOf("", " ", "   ")
}

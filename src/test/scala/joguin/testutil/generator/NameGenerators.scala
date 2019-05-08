package joguin.testutil.generator

import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.Name
import joguin.NameR
import org.scalacheck.Gen

object NameGenerators {
  def genName: Gen[Name] = {
    val elseName: Name = "Fallback Name"

    Gen.alphaStr
      .map(refineV[NameR](_))
      .map(_.getOrElse(elseName))
  }

  def genInvalidName: Gen[String] =
    Gen.oneOf("", " ", "   ")
}

package joguin.testutil.generator

import joguin.earth.maincharacter.Gender
import joguin.earth.maincharacter.Gender._
import org.scalacheck.Gen

object GenderGenerators {
  def genGender: Gen[Gender] =
    Gen.oneOf(Female, Male, Other)
}

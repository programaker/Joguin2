package joguin.testutil.generator

import joguin.earth.maincharacter.Female
import joguin.earth.maincharacter.Gender
import joguin.earth.maincharacter.Male
import joguin.earth.maincharacter.Other
import org.scalacheck.Gen

object GenderGenerators {
  def genGender: Gen[Gender] =
    Gen.oneOf(Female, Male, Other)
}

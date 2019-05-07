package joguin.testutil.generator

import joguin.earth.maincharacter.{Female, Gender, Male, Other}
import org.scalacheck.Gen

object GenderGenerators {
  def genGender: Gen[Gender] =
    Gen.oneOf(Female, Male, Other)
}

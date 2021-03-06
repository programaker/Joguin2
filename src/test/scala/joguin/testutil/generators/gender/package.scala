package joguin.testutil.generators

import joguin.earth.maincharacter.Gender
import joguin.earth.maincharacter.Gender.Female
import joguin.earth.maincharacter.Gender.Male
import joguin.earth.maincharacter.Gender.Other
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

package object gender {
  implicit val gender: Arbitrary[Gender] = Arbitrary(genGender)
  def genGender: Gen[Gender] = Gen.oneOf(Female, Male, Other)
}

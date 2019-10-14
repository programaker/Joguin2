package joguin.testutil.generators

import joguin.earth.maincharacter.MainCharacter
import name._
import joguin.testutil.generators.age.genValidAge
import joguin.testutil.generators.experience.genValidExperience
import joguin.testutil.generators.gender.genGender
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

package object maincharacter {
  implicit val mainCharacter: Arbitrary[MainCharacter] = Arbitrary(genMainCharacter)

  def genMainCharacter: Gen[MainCharacter] =
    for {
      name   <- genValidName
      gender <- genGender
      age    <- genValidAge
      xp     <- genValidExperience
    } yield MainCharacter(name, gender, age, xp)
}

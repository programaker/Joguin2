package joguin.testutil.generators

import joguin.earth.maincharacter.MainCharacter
import name._
import joguin.testutil.generators.age.genValidAge
import joguin.testutil.generators.experience.genValidExperience
import joguin.testutil.generators.gender.genGender
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._
import cats.syntax.apply._

package object maincharacter {
  implicit val mainCharacter: Arbitrary[MainCharacter] = Arbitrary(genMainCharacter)

  def genMainCharacter: Gen[MainCharacter] =
    (genValidName, genGender, genValidAge, genValidExperience).mapN(MainCharacter)
}

package joguin.testutil.generators

import joguin.earth.maincharacter.MainCharacter
import name._
import joguin.testutil.generators.age.genAge
import joguin.testutil.generators.experience.genExperience
import joguin.testutil.generators.gender.genGender
import org.scalacheck.Gen

package object maincharacter {
  def genMainCharacter: Gen[MainCharacter] =
    for {
      name   <- genName
      gender <- genGender
      age    <- genAge
      xp     <- genExperience
    } yield MainCharacter(name, gender, age, xp)
}

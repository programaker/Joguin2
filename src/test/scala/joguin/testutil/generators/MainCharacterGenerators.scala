package joguin.testutil.generators

import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.generators.NameGenerators.genName
import joguin.testutil.generators.age._
import joguin.testutil.generators.experience._
import joguin.testutil.generators.gender._
import org.scalacheck.Gen

object MainCharacterGenerators {
  def genMainCharacter: Gen[MainCharacter] =
    for {
      name   <- genName
      gender <- genGender
      age    <- genAge
      xp     <- genExperience
    } yield MainCharacter(name, gender, age, xp)
}

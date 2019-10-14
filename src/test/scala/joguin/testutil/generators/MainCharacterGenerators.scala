package joguin.testutil.generators

import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.generators.ExperienceGenerators.genExperience
import joguin.testutil.generators.GenderGenerators.genGender
import joguin.testutil.generators.NameGenerators.genName
import joguin.testutil.generators.age._
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

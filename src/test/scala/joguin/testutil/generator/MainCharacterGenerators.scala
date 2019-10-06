package joguin.testutil.generator

import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.generator.AgeGenerators.genAge
import joguin.testutil.generator.ExperienceGenerators.genExperience
import joguin.testutil.generator.GenderGenerators.genGender
import joguin.testutil.generator.NameGenerators.genName
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

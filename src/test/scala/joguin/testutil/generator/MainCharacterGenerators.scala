package joguin.testutil.generator

import cats.implicits._
import joguin.earth.maincharacter.MainCharacter
import joguin.game.progress.PersistentMainCharacter
import joguin.testutil.generator.AgeGenerators.genAge
import joguin.testutil.generator.GenderGenerators.genGender
import joguin.testutil.generator.NameGenerators.genName
import joguin.testutil.generator.ExperienceGenerators.genExperience
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._

object MainCharacterGenerators {
  def genMainCharacter: Gen[MainCharacter] =
    (genName, genGender, genAge, genExperience).mapN(MainCharacter.apply)

  def genPersistentMainCharacter: Gen[PersistentMainCharacter] =
    (genName, genGender, genAge, genExperience).mapN { (name, gender, age, experience) =>
      PersistentMainCharacter(name.value, gender, age.value, experience.value)
    }
}

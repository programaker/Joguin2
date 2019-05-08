package joguin.testutil.generator

import cats.implicits._
import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.generator.AgeGenerators.genAge
import joguin.testutil.generator.GenderGenerators.genGender
import joguin.testutil.generator.NameGenerators.genName
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._

object MainCharacterGenerators {
  def genMainCharacter: Gen[MainCharacter] =
    (genName, genGender, genAge).mapN(MainCharacter.apply)
}

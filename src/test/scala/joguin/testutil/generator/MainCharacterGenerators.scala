package joguin.testutil.generator

import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.generator.AgeGenerators.{genAge, genInvalidAge}
import joguin.testutil.generator.GenderGenerators.genGender
import joguin.testutil.generator.NameGenerators.{genInvalidName, genName}
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._
import cats.implicits._
import joguin.game.progress.PersistentMainCharacter

object MainCharacterGenerators {
  def genMainCharacter: Gen[MainCharacter] =
    (genName, genGender, genAge).mapN(MainCharacter.apply)

  def genInvalidPersistentMainCharacter: Gen[PersistentMainCharacter] =
    (genInvalidName, genGender, genInvalidAge).mapN(PersistentMainCharacter.apply)
}

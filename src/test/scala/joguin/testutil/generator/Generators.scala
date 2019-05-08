package joguin.testutil.generator

import joguin.alien.Invasion
import joguin.earth.city.City
import joguin.earth.maincharacter._
import joguin.game.progress._
import joguin.testutil.generator.CityGenerators._
import joguin.testutil.generator.ExperienceGenerators._
import joguin.testutil.generator.IndexGenerator._
import joguin.testutil.generator.InvasionGenerators._
import joguin.testutil.generator.MainCharacterGenerators._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

object Generators {
  implicit val city: Arbitrary[City] =
    Arbitrary(genCity)

  implicit val invalidCity: Arbitrary[String] =
    Arbitrary(genInvalidCity)

  implicit val mainCharacter: Arbitrary[MainCharacter] =
    Arbitrary(genMainCharacter)

  implicit val invalidPersistentMainCharacter: Arbitrary[PersistentMainCharacter] =
    Arbitrary(genInvalidPersistentMainCharacter)

  implicit val invasionList: Arbitrary[List[Invasion]] =
    Arbitrary(genInvasionList)

  implicit val persistentInvasionList: Arbitrary[List[PersistentInvasion]] =
    Arbitrary(genPersistentInvasionList)

  implicit val defeatedInvasions: Arbitrary[Int] =
    Arbitrary(genDefeatedInvasions)

  implicit val defeatedInvasionsTrack: Arbitrary[List[Int]] =
    Arbitrary(genDefeatedInvasionsTrack)

  implicit val index: Arbitrary[Index] =
    Arbitrary(genValidIndex(invasionListSize))

  implicit val invalidIndex: Arbitrary[Index] =
    Arbitrary(genInvalidIndex(invasionListSize))

  implicit val experience: Arbitrary[Experience] =
    Arbitrary(genExperience)

  implicit val smallInt: Arbitrary[Int] =
    Arbitrary(Gen.choose(min = 1, max = 10))
}

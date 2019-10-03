package joguin.testutil.generator

import joguin.Name
import joguin.alien.Invasion
import joguin.earth.city.City
import joguin.earth.maincharacter._
import joguin.game.progress._
import joguin.testutil.generator.CityGenerators._
import joguin.testutil.generator.ExperienceGenerators._
import joguin.testutil.generator.IndexGenerator._
import joguin.testutil.generator.InvasionGenerators._
import joguin.testutil.generator.MainCharacterGenerators._
import joguin.testutil.generator.NameGenerators._
import joguin.testutil.generator.GenderGenerators._
import joguin.testutil.generator.AgeGenerators._
import joguin.testutil.generator.GameProgressGenerators._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

object Generators {
  implicit val city: Arbitrary[City] =
    Arbitrary(genCity)

  implicit val invalidCity: Arbitrary[String] =
    Arbitrary(genInvalidCity)

  implicit val mainCharacter: Arbitrary[MainCharacter] =
    Arbitrary(genMainCharacter)

  implicit val persistentMainCharacter: Arbitrary[PersistentMainCharacter] =
    Arbitrary(genPersistentMainCharacter)

  implicit val invasionList: Arbitrary[Vector[Invasion]] =
    Arbitrary(genInvasionList)

  implicit val persistentInvasionList: Arbitrary[List[PersistentInvasion]] =
    Arbitrary(genPersistentInvasionList)

  implicit val defeatedInvasions: Arbitrary[Count] =
    Arbitrary(genDefeatedInvasions)

  implicit val defeatedInvasionsTrack: Arbitrary[List[Index]] =
    Arbitrary(genDefeatedInvasionsTrack)

  implicit val index: Arbitrary[Index] =
    Arbitrary(genValidIndex(invasionListSize))

  implicit val invalidIndex: Arbitrary[Index] =
    Arbitrary(genInvalidIndex(invasionListSize))

  implicit val experience: Arbitrary[Experience] =
    Arbitrary(genExperience)

  implicit val invalidExperience: Arbitrary[Int] =
    Arbitrary(genInvalidExperience)

  implicit val smallInt: Arbitrary[Int] =
    Arbitrary(genSmallInt)

  implicit val name: Arbitrary[Name] =
    Arbitrary(genName)

  implicit val invalidName: Arbitrary[String] =
    Arbitrary(genInvalidName)

  implicit val gender: Arbitrary[Gender] =
    Arbitrary(genGender)

  implicit val age: Arbitrary[Age] =
    Arbitrary(genAge)

  implicit val invalidAge: Arbitrary[Int] =
    Arbitrary(genInvalidAge)

  implicit val gameProgressStart: Arbitrary[GameProgress] =
    Arbitrary(genGameProgressStart)

  implicit val quitOption: Arbitrary[String] =
    Arbitrary(genQuitOption)

  def genSmallInt: Gen[Int] =
    Gen.choose(min = 1, max = 10)

  def genQuitOption: Gen[String] = Gen.oneOf("q", "Q")

  def arbitraryTag[T, A](gen: Gen[A]): Arbitrary[Tag[T, A]] =
    Arbitrary(gen.map(Tag.apply))
}

package joguin.testutil.generators

import joguin.IdxSeq
import joguin.Name
import joguin.alien.Invasion
import joguin.earth.city.City
import joguin.earth.maincharacter._
import joguin.game.progress._
import joguin.testutil.generators.GameProgressGenerators._
import joguin.testutil.generators.GenderGenerators._
import joguin.testutil.generators.IndexGenerator._
import joguin.testutil.generators.InvasionGenerators._
import joguin.testutil.generators.MainCharacterGenerators._
import joguin.testutil.generators.NameGenerators._
import joguin.testutil.generators.age._
import joguin.testutil.generators.city._
import joguin.testutil.generators.experience._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

object Generators {
  implicit val city: Arbitrary[City] =
    Arbitrary(genCity)

  implicit val invalidCity: Arbitrary[String] =
    Arbitrary(genInvalidCity)

  implicit val mainCharacter: Arbitrary[MainCharacter] =
    Arbitrary(genMainCharacter)

  implicit val invasionSeq: Arbitrary[IdxSeq[Invasion]] =
    Arbitrary(genInvasionSeq(defeated = false))

  implicit val defeatedInvasions: Arbitrary[IdxSeq[Invasion]] =
    Arbitrary(genInvasionSeq(defeated = true))

  implicit val index: Arbitrary[Index] =
    Arbitrary(genValidIndex(invasionSeqSize))

  implicit val invalidIndex: Arbitrary[Index] =
    Arbitrary(genInvalidIndex(invasionSeqSize))

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

  def genQuitOption: Gen[String] =
    Gen.oneOf("q", "Q")

  def arbitraryTag[T, A](gen: Gen[A]): Arbitrary[Tag[T, A]] =
    Arbitrary(gen.map(Tag.apply))
}
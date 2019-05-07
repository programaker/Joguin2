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
import org.scalacheck.{Arbitrary, Gen}

object Generators {
  implicit def city: Arbitrary[City] = Arbitrary(genCity)
  implicit def invalidCity: Arbitrary[String] = Arbitrary(genInvalidCity)

  implicit def mainCharacter: Arbitrary[MainCharacter] = Arbitrary(genMainCharacter)

  implicit def invasionList: Arbitrary[List[Invasion]] = Arbitrary(genInvasionList)

  implicit def index: Arbitrary[Index] = Arbitrary(genValidIndex(invasionListSize))
  implicit def invalidIndex: Arbitrary[Index] = Arbitrary(genInvalidIndex(invasionListSize))

  implicit def experience: Arbitrary[Experience] = Arbitrary(genExperience)

  implicit def intFrom1: Arbitrary[Int] = Arbitrary(Gen.choose(min = 1, max = Int.MaxValue))

  /*implicit val arbCity: Arbitrary[City] = Arbitrary(genCity)
  implicit val arbMainCharacter: Arbitrary[MainCharacter] = Arbitrary(genMainCharacter)
  implicit val arbInvasionList: Arbitrary[List[Invasion]] = Arbitrary(genInvasionList)
  implicit val arbIndex: Arbitrary[Index] = Arbitrary(genIndex(min = 1, max = invasionListSize * 2))
  implicit val arbExperience: Arbitrary[Experience] = Arbitrary(genExperience)
  implicit val arbInt: Arbitrary[Int] = Arbitrary(Gen.choose(min = 1, max = 100))*/
}

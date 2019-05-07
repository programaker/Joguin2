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
import org.scalacheck.Gen

object Generators {
  def city: Gen[City] = genCity
  def invalidCity: Gen[String] = genInvalidCity

  def mainCharacter: Gen[MainCharacter] = genMainCharacter

  def invasionList: Gen[List[Invasion]] = genInvasionList

  def index: Gen[Index] = genValidIndex(invasionListSize)
  def invalidIndex: Gen[Index] = genInvalidIndex(invasionListSize)

  def experience: Gen[Experience] = genExperience

  def intFrom1: Gen[Int] = Gen.choose(min = 1, max = 10)

  /*implicit val arbCity: Arbitrary[City] = Arbitrary(genCity)
  implicit val arbMainCharacter: Arbitrary[MainCharacter] = Arbitrary(genMainCharacter)
  implicit val arbInvasionList: Arbitrary[List[Invasion]] = Arbitrary(genInvasionList)
  implicit val arbIndex: Arbitrary[Index] = Arbitrary(genIndex(min = 1, max = invasionListSize * 2))
  implicit val arbExperience: Arbitrary[Experience] = Arbitrary(genExperience)
  implicit val arbInt: Arbitrary[Int] = Arbitrary(Gen.choose(min = 1, max = 100))*/
}

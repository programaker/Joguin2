package joguin.testutil

import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.alien.terraformdevice.TerraformDevice
import joguin.alien.{AlienArmy, Invasion, Power, PowerR}
import joguin.{Name, NameR}
import joguin.earth.{Country, CountryR}
import joguin.earth.city.City
import joguin.earth.maincharacter.{Age, AgeR, Female, Gender, MainCharacter, Male, Other}
import joguin.game.progress.{Index, IndexR}
import org.scalacheck.{Arbitrary, Gen}

object Generators {
  implicit val arbCity: Arbitrary[City] = Arbitrary(genCity)
  implicit val arbMainCharacter: Arbitrary[MainCharacter] = Arbitrary(genMainCharacter)
  implicit val arbInvasionList: Arbitrary[List[Invasion]] = Arbitrary(genInvasionList)
  implicit val arbIndex: Arbitrary[Index] = Arbitrary(genIndex)


  def genInvasionList: Gen[List[Invasion]] = {
    Gen.containerOfN[List, Invasion](10, genInvasion)
  }

  def genInvasion: Gen[Invasion] =
    for {
      power <- genPower
      city <- genCity
    } yield Invasion(TerraformDevice(power), city)

  def genMainCharacter: Gen[MainCharacter] =
    for {
      name <- genName
      gender <- genGender
      age <- genAge
    } yield MainCharacter(name, gender, age)

  def genCity: Gen[City] =
    for {
      name <- genName
      country <- genCountry
    } yield City(name, country)

  def genName: Gen[Name] = {
    val elseName: Name = "Fallback Name"

    Gen.alphaStr
      .map(refineV[NameR](_))
      .map(_.getOrElse(elseName))
  }

  def genCountry: Gen[Country] = {
    val elseCountry: Country = "Fallback Country"

    Gen.alphaStr
      .map(refineV[CountryR](_))
      .map(_.getOrElse(elseCountry))
  }

  def genAge: Gen[Age] = {
    val elseAge: Age = 18

    Gen.choose(min = 18, max = 65)
      .map(refineV[AgeR](_))
      .map(_.getOrElse(elseAge))
  }

  def genGender: Gen[Gender] =
    Gen.oneOf(Female, Male, Other)

  def genPower: Gen[Power] = {
    val elsePower: Power = AlienArmy.minPower

    Gen.choose(min = AlienArmy.minPower.value, max = AlienArmy.maxPower.value)
      .map(refineV[PowerR](_))
      .map(_.getOrElse(elsePower))
  }

  def genIndex: Gen[Index] = {
    val elseIndex: Index = 1

    Gen.choose(min = 1, max = 20)
      .map(refineV[IndexR](_))
      .map(_.getOrElse(elseIndex))
  }
}

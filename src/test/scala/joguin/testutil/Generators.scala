package joguin.testutil

import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.alien.{AlienArmy, Invasion, Power, PowerR}
import joguin.{Name, NameR}
import joguin.earth.{Country, CountryR}
import joguin.earth.city.City
import joguin.earth.maincharacter.{Age, AgeR, Female, Gender, MainCharacter, Male, Other}
import org.scalacheck.{Arbitrary, Gen}

object Generators {
  implicit val arbCity: Arbitrary[City] = Arbitrary(genCity)
  implicit val arbMC: Arbitrary[MainCharacter] = Arbitrary(genMainCharacter)

  def genInvasion: Gen[Invasion] =
    for {
      mc <- genMainCharacter
      city <- genCity
    } yield Invasion()

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
}

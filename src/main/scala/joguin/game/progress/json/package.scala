package joguin.game.progress

import cats.syntax.all._
import eu.timepit.refined.refineV
import joguin.NameR
import joguin.alien.PowerR
import joguin.alien.invasion.Invasion
import joguin.alien.terraformdevice.TerraformDevice
import joguin.earth.city.City
import joguin.earth.CountryR
import joguin.earth.maincharacter.Gender
import joguin.earth.maincharacter.ExperienceR
import joguin.earth.maincharacter.AgeR
import joguin.earth.maincharacter.MainCharacter
import joguin.earth.maincharacter.parseGender

package object json {
  def makeGameProgressDto(gp: GameProgress): GameProgressDto =
    GameProgressDto(
      mainCharacter = makeMainCharacterDto(gp.mainCharacter),
      invasions = gp.invasions.map(makeInvasionDto)
    )

  def gameProgressFromDto(gpd: GameProgressDto): Either[String, GameProgress] =
    ???

  private def makeMainCharacterDto(mc: MainCharacter): MainCharacterDto =
    MainCharacterDto(
      name = mc.name.value,
      gender = mc.gender.code,
      age = mc.age.value,
      experience = mc.experience.value
    )

  private def mainCharacterFromDto(mcd: MainCharacterDto): Either[String, MainCharacter] =
    (
      refineV[NameR](mcd.name),
      parseGender(mcd.gender).toRight(s"Invalid Gender: ${mcd.gender}"),
      refineV[AgeR](mcd.age),
      refineV[ExperienceR](mcd.experience)
    ).mapN(MainCharacter.apply)

  private def makeInvasionDto(invasion: Invasion): InvasionDto =
    InvasionDto(
      terraformDevice = makeTerraformDeviceDto(invasion.terraformDevice),
      city = makeCityDto(invasion.city),
      defeated = invasion.defeated
    )

  private def invasionFromDto(id: InvasionDto): Either[String, Invasion] =
    (terraformDeviceFromDto(id.terraformDevice), cityFromDto(id.city), Right(id.defeated)).mapN(Invasion.apply)

  private def makeTerraformDeviceDto(td: TerraformDevice): TerraformDeviceDto =
    TerraformDeviceDto(defensePower = td.defensePower.value)

  private def terraformDeviceFromDto(tdd: TerraformDeviceDto): Either[String, TerraformDevice] =
    refineV[PowerR](tdd.defensePower).map(TerraformDevice.apply)

  private def makeCityDto(city: City): CityDto =
    CityDto(name = city.name.value, country = city.country.value)

  private def cityFromDto(cd: CityDto): Either[String, City] =
    (refineV[NameR](cd.name), refineV[CountryR](cd.country)).mapN(City.apply)
}

package joguin.game.progress

import cats.implicits._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.NameR
import joguin.alien.terraformdevice.TerraformDevice
import joguin.alien.{Invasion, PowerR}
import joguin.earth.city.City
import joguin.earth.maincharacter.{AgeR, Gender, MainCharacter}

final case class PersistentGameProgress(
  mainCharacter: PersistentMainCharacter,
  mainCharacterExperience: Int,
  invasions: List[PersistentInvasion],
  defeatedInvasions: Int,
  defeatedInvasionsTrack: List[Int]
)
object PersistentGameProgress {
  import PersistentInvasion._
  import PersistentMainCharacter._

  def fromGameProgress(gp: GameProgress): PersistentGameProgress = PersistentGameProgress(
    mainCharacter = fromMainCharacter(gp.mainCharacter),
    mainCharacterExperience = gp.mainCharacterExperience,
    invasions = gp.invasions.map(fromInvasion),
    defeatedInvasions = gp.defeatedInvasions,
    defeatedInvasionsTrack = gp.defeatedInvasionsTrack.map(_.value).toList
  )

  def toGameProgress(pgp: PersistentGameProgress): Option[GameProgress] =
    (
      toMainCharacter(pgp.mainCharacter),
      refineV[ExperienceR](pgp.mainCharacterExperience).toOption,
      pgp.invasions.map(toInvasion).sequence[Option, Invasion],
      refineV[CountR](pgp.defeatedInvasions).toOption,
      pgp.defeatedInvasionsTrack.flatMap(refineV[IndexR](_).toList).toSet.some
    ) mapN { (mc, mcxp, invs, dinvs, track) =>
      GameProgress.of(mc, mcxp, invs, dinvs, track)
    }
}

final case class PersistentMainCharacter(
  name: String,
  gender: Gender,
  age: Int
)
object PersistentMainCharacter {
  def fromMainCharacter(mc: MainCharacter): PersistentMainCharacter = PersistentMainCharacter(
    name = mc.name,
    gender = mc.gender,
    age = mc.age
  )

  def toMainCharacter(pmc: PersistentMainCharacter): Option[MainCharacter] =
    (
      refineV[NameR](pmc.name).toOption,
      pmc.gender.some,
      refineV[AgeR](pmc.age).toOption
    ) mapN { (name, gender, age) =>
      MainCharacter(name, gender, age)
    }
}

final case class PersistentInvasion(
  terraformDevicePower: Int,
  cityName: String,
  country: String
)
object PersistentInvasion {
  def fromInvasion(i: Invasion): PersistentInvasion = PersistentInvasion(
    terraformDevicePower = i.terraformDevice.defensePower,
    cityName = i.city.name,
    country = i.city.country
  )

  def toInvasion(pi: PersistentInvasion): Option[Invasion] =
    (
      refineV[PowerR](pi.terraformDevicePower).toOption,
      refineV[NameR](pi.cityName).toOption,
      refineV[NameR](pi.country).toOption
    ) mapN { (power, cityName, country) =>
      Invasion(TerraformDevice(power), City(cityName, country))
    }
}

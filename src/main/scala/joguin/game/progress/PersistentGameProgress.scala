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
) {
	def toGameProgress: Option[GameProgress] =
    (
      mainCharacter.toMainCharacter,
      refineV[ExperienceR](mainCharacterExperience).toOption,
      invasions.map(_.toInvasion).sequence[Option, Invasion],
      refineV[CountR](defeatedInvasions).toOption,
      defeatedInvasionsTrack.flatMap(refineV[IndexR](_).toList).toSet.some
    ) mapN { (mc, mcxp, invs, dinvs, track) =>
      GameProgress.of(mc, mcxp, invs, dinvs, track)
    }
}

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
}


final case class PersistentMainCharacter(
  name: String,
  gender: Gender,
  age: Int
) {
	def toMainCharacter: Option[MainCharacter] =
    (
      refineV[NameR](name).toOption,
      gender.some,
      refineV[AgeR](age).toOption
    ) mapN { (name, gender, age) =>
      MainCharacter(name, gender, age)
    }
}

object PersistentMainCharacter {
  def fromMainCharacter(mc: MainCharacter): PersistentMainCharacter = PersistentMainCharacter(
    name = mc.name,
    gender = mc.gender,
    age = mc.age
  )
}


final case class PersistentInvasion(
  terraformDevicePower: Int,
  cityName: String,
  country: String
) {
	def toInvasion: Option[Invasion] =
    (
      refineV[PowerR](terraformDevicePower).toOption,
      refineV[NameR](cityName).toOption,
      refineV[NameR](country).toOption
    ) mapN { (power, cityName, country) =>
      Invasion(TerraformDevice(power), City(cityName, country))
    }
}

object PersistentInvasion {
  def fromInvasion(i: Invasion): PersistentInvasion = PersistentInvasion(
    terraformDevicePower = i.terraformDevice.defensePower,
    cityName = i.city.name,
    country = i.city.country
  )
}

package joguin.game.progress

import cats.implicits._
import joguin.earth.maincharacter.{MainCharacter, Major}
import joguin.earth.maincharacter.MainCharacter.Gender
import joguin.NonBlankString
import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.{NonNegative, Positive}
import joguin.alien.Invasion
import joguin.alien.terraformdevice.TerraformDevice
import joguin.earth.city.City


final case class PersistentGameProgress (
  mainCharacter: PersistentMainCharacter,
  mainCharacterExperience: Int,
  invasions: List[PersistentInvasion],
  defeatedInvasions: Int,
  defeatedInvasionsTrack: List[Int]
)

object PersistentGameProgress {
  import PersistentMainCharacter._
  import PersistentInvasion._

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
      refineV[NonNegative](pgp.mainCharacterExperience).toOption,
      pgp.invasions.map(toInvasion).sequence[Option,Invasion],
      refineV[NonNegative](pgp.defeatedInvasions).toOption,
      pgp.defeatedInvasionsTrack.flatMap(refineV[Positive](_).toList).toSet.some
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
      refineV[NonBlankString](pmc.name).toOption,
      pmc.gender.some,
      refineV[Major](pmc.age).toOption
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
      refineV[Positive](pi.terraformDevicePower).toOption,
      refineV[NonBlankString](pi.cityName).toOption,
      refineV[NonBlankString](pi.country).toOption
    ) mapN { (power, cityName, country) =>
      Invasion(TerraformDevice(power), City(cityName, country))
    }
}

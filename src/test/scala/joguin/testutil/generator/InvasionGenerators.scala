package joguin.testutil.generator

import joguin.alien.Invasion
import joguin.alien.terraformdevice.TerraformDevice
import joguin.game.progress.PersistentInvasion
import joguin.testutil.generator.CityGenerators.{genCity, genInvalidCity}
import joguin.testutil.generator.CountryGenerators.genInvalidCountry
import joguin.testutil.generator.PowerGenerators._
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._
import cats.implicits._

object InvasionGenerators {
  val invasionListSize: Int = 10

  def genInvasionList: Gen[List[Invasion]] =
    Gen.listOfN[Invasion](invasionListSize, genInvasion)

  def genInvasion: Gen[Invasion] =
    (genPower, genCity)
      .mapN((power, city) => Invasion(TerraformDevice(power), city))

  def genInvalidPersistentInvasion: Gen[PersistentInvasion] =
    (genInvalidPower, genInvalidCity, genInvalidCountry)
      .mapN(PersistentInvasion.apply)
}

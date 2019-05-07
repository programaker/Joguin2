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
    Gen.listOfN(invasionListSize, genInvasion)

  def genPersistentInvasionList: Gen[List[PersistentInvasion]] =
    Gen.listOfN(invasionListSize, genPersistentInvasion)

  def genInvasion: Gen[Invasion] =
    (genPower, genCity).mapN { (power, city) =>
      Invasion(TerraformDevice(power), city)
    }

  def genPersistentInvasion: Gen[PersistentInvasion] =
    genInvasion.map(PersistentInvasion.fromInvasion)

  def genInvalidPersistentInvasion: Gen[PersistentInvasion] =
    (genInvalidPower, genInvalidCity, genInvalidCountry)
      .mapN(PersistentInvasion.apply)

  def genDefeatedInvasions: Gen[Int] =
    Gen.choose(min = 1, max = invasionListSize)

  def genDefeatedInvasionsTrack: Gen[List[Int]] =
    genDefeatedInvasions.flatMap(n => Gen.listOfN(n, genDefeatedInvasions))
}

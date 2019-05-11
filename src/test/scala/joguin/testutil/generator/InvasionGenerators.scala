package joguin.testutil.generator

import cats.implicits._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.alien.Invasion
import joguin.alien.terraformdevice.TerraformDevice
import joguin.game.progress.Count
import joguin.game.progress.CountR
import joguin.game.progress.Index
import joguin.game.progress.PersistentInvasion
import joguin.testutil.generator.CityGenerators.genCity
import joguin.testutil.generator.CityGenerators.genInvalidCity
import joguin.testutil.generator.CountryGenerators.genInvalidCountry
import joguin.testutil.generator.IndexGenerator._
import joguin.testutil.generator.NameGenerators._
import joguin.testutil.generator.PowerGenerators._
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._

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
    (genPower, genName, genName).mapN { (power, city, country) =>
      PersistentInvasion(power.value, city, country)
    }

  def genInvalidPersistentInvasion: Gen[PersistentInvasion] =
    (genInvalidPower, genInvalidCity, genInvalidCountry)
      .mapN(PersistentInvasion.apply)

  def genDefeatedInvasions: Gen[Count] = {
    val elseValue: Count = 0

    Gen.choose(min = 0, max = invasionListSize)
      .map(refineV[CountR](_).getOrElse(elseValue))
  }

  def genDefeatedInvasionsTrack: Gen[List[Index]] =
    genDefeatedInvasions.flatMap { n =>
      Gen.listOfN(n.value, genValidIndex(invasionListSize))
    }
}
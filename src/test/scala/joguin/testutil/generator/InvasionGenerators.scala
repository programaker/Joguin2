package joguin.testutil.generator

import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.alien.Invasion
import joguin.alien.terraformdevice.TerraformDevice
import joguin.game.progress.Count
import joguin.game.progress.CountR
import joguin.game.progress.Index
import joguin.testutil.generator.CityGenerators.genCity
import joguin.testutil.generator.IndexGenerator._
import joguin.testutil.generator.PowerGenerators._
import org.scalacheck.Gen

object InvasionGenerators {
  val invasionListSize: Int = 10

  def genInvasionList: Gen[Vector[Invasion]] =
    Gen.containerOfN[Vector, Invasion](invasionListSize, genInvasion)

  def genInvasion: Gen[Invasion] =
    for {
      power <- genPower
      city  <- genCity
    } yield Invasion(TerraformDevice(power), city)

  def genDefeatedInvasions: Gen[Count] =
    Gen
      .choose(min = 0, max = invasionListSize)
      .map(refineV[CountR](_).getOrElse(0: Count))

  def genDefeatedInvasionsTrack: Gen[List[Index]] =
    genDefeatedInvasions.flatMap { n =>
      Gen.listOfN(n.value, genValidIndex(invasionListSize))
    }
}

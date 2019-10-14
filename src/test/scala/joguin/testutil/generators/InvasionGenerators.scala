package joguin.testutil.generators

import joguin.IdxSeq
import joguin.alien.Invasion
import joguin.alien.terraformdevice.TerraformDevice
import joguin.testutil.generators.CityGenerators.genCity
import joguin.testutil.generators.PowerGenerators._
import org.scalacheck.Gen

object InvasionGenerators {
  val invasionSeqSize: Int = 10

  def genInvasionSeq(defeated: Boolean): Gen[IdxSeq[Invasion]] =
    Gen.containerOfN[IdxSeq, Invasion](invasionSeqSize, genInvasion(defeated))

  def genInvasion(defeated: Boolean): Gen[Invasion] =
    for {
      power <- genPower
      city  <- genCity
    } yield Invasion(TerraformDevice(power), city, defeated)
}

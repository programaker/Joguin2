package joguin.testutil.generators

import joguin.IdxSeq
import joguin.alien.Invasion
import joguin.alien.terraformdevice.TerraformDevice
import joguin.testutil.generators.PowerGenerators.genPower
import joguin.testutil.generators.city.genCity
import org.scalacheck.Gen

package object invasion {
  val InvasionSeqSize: Int = 10

  def genInvasionSeq(defeated: Boolean): Gen[IdxSeq[Invasion]] =
    Gen.containerOfN[IdxSeq, Invasion](InvasionSeqSize, genInvasion(defeated))

  def genInvasion(defeated: Boolean): Gen[Invasion] =
    for {
      power <- genPower
      city  <- genCity
    } yield Invasion(TerraformDevice(power), city, defeated)
}

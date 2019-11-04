package joguin.testutil.generators

import joguin.IdxSeq
import joguin.alien.invasion
import joguin.alien.invasion.Invasion
import joguin.alien.terraformdevice.TerraformDevice
import joguin.testutil.generators.city.genValidCity
import joguin.testutil.generators.powergenerator._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

package object invasion {
  val InvasionSeqSize: Int = 10

  implicit val invasionSeq: Arbitrary[IdxSeq[Invasion]] =
    Arbitrary(genInvasionSeq(defeated = false))

  implicit val defeatedInvasions: Arbitrary[IdxSeq[Invasion]] =
    Arbitrary(genInvasionSeq(defeated = true))

  def genInvasionSeq(defeated: Boolean): Gen[IdxSeq[Invasion]] =
    Gen.containerOfN[IdxSeq, Invasion](InvasionSeqSize, genInvasion(defeated))

  def genInvasion(defeated: Boolean): Gen[Invasion] =
    for {
      power <- genValidPower
      city  <- genValidCity
    } yield invasion.Invasion(TerraformDevice(power), city, defeated)
}

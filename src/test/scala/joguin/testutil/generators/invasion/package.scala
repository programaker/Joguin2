package joguin.testutil.generators

import cats.implicits._
import joguin.IdxSeq
import joguin.alien.invasion.Invasion
import joguin.alien.terraformdevice.TerraformDevice
import joguin.testutil.generators.city.genValidCity
import joguin.testutil.generators.powergenerator._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._

package object invasion {
  val InvasionSeqSize: Int = 10

  implicit val invasionSeq: Arbitrary[IdxSeq[Invasion]] =
    Arbitrary(genInvasionSeq(defeated = false))

  implicit val defeatedInvasions: Arbitrary[IdxSeq[Invasion]] =
    Arbitrary(genInvasionSeq(defeated = true))

  def genInvasionSeq(defeated: Boolean): Gen[IdxSeq[Invasion]] =
    Gen.containerOfN[IdxSeq, Invasion](InvasionSeqSize, genInvasion(defeated))

  def genInvasion(defeated: Boolean): Gen[Invasion] =
    (genValidPower, genValidCity).mapN((power, city) => Invasion(TerraformDevice(power), city, defeated))
}

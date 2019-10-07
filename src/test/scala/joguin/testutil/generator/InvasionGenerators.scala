package joguin.testutil.generator

import joguin.alien.Invasion
import joguin.alien.terraformdevice.TerraformDevice
import joguin.testutil.generator.CityGenerators.genCity
import joguin.testutil.generator.PowerGenerators._
import org.scalacheck.Gen

object InvasionGenerators {
  val invasionListSize: Int = 10

  def genInvasionList(defeated: Boolean): Gen[Vector[Invasion]] =
    Gen.containerOfN[Vector, Invasion](invasionListSize, genInvasion(defeated))

  def genInvasion(defeated: Boolean): Gen[Invasion] =
    for {
      power <- genPower
      city  <- genCity
    } yield Invasion(TerraformDevice(power), city, defeated)
}

package joguin.testutil.generators

import joguin.game.progress.GameProgress
import joguin.testutil.generators.invasion._
import joguin.testutil.generators.maincharacter._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._
import cats.syntax.apply._

package object gameprogress {
  implicit val gameProgressStart: Arbitrary[GameProgress] =
    Arbitrary(genGameProgressStart)

  def genGameProgressStart: Gen[GameProgress] =
    (genMainCharacter, genInvasionSeq(defeated = false)).mapN(GameProgress)
}

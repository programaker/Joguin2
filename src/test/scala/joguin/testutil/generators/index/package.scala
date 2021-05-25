package joguin.testutil.generators

import joguin.refined.auto._
import eu.timepit.refined.refineV
import joguin.game.progress.Index
import joguin.game.progress.IndexR
import joguin.testutil.generators.invasion._
import org.scalacheck.Arbitrary
import org.scalacheck.Gen

package object index {
  implicit val validIndex: Arbitrary[Index] =
    Arbitrary(genValidIndex(InvasionSeqSize))

  implicit val invalidIndex: Arbitrary[Index] =
    Arbitrary(genInvalidIndex(InvasionSeqSize))

  def genValidIndex(lastIndex: Int): Gen[Index] =
    genIndex(1, lastIndex)

  def genInvalidIndex(lastIndex: Int): Gen[Index] =
    genIndex(lastIndex + 1, lastIndex * 2)

  def genIndex(min: Int, max: Int): Gen[Index] =
    Gen
      .choose(min, max)
      .map(refineV[IndexR](_))
      .map(_.getOrElse(1))
}

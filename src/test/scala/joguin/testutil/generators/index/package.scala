package joguin.testutil.generators

import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.game.progress.Index
import joguin.game.progress.IndexR
import org.scalacheck.Gen

package object index {
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

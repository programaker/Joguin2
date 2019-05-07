package joguin.testutil.generator

import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.game.progress.{Index, IndexR}
import org.scalacheck.Gen

object IndexGenerator {
  def genValidIndex(lastIndex: Int): Gen[Index] =
    genIndex(1, lastIndex)

  def genInvalidIndex(lastIndex: Int): Gen[Index] =
    genIndex(lastIndex + 1, lastIndex * 2)

  def genIndex(min: Int, max: Int): Gen[Index] = {
    val elseIndex: Index = 1

    Gen.choose(min, max)
      .map(refineV[IndexR](_))
      .map(_.getOrElse(elseIndex))
  }
}

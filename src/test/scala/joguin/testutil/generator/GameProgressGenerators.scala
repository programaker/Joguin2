package joguin.testutil.generator

import cats.implicits._
import joguin.game.progress.GameProgress
import joguin.testutil.generator.InvasionGenerators._
import joguin.testutil.generator.MainCharacterGenerators._
import org.scalacheck.Gen
import org.scalacheck.cats.implicits._

object GameProgressGenerators {
  def genGameProgressStart: Gen[GameProgress] =
    (genMainCharacter, genInvasionList).mapN(GameProgress.start)
}

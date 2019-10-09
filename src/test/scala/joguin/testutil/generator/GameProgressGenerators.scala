package joguin.testutil.generator

import joguin.game.progress._
import joguin.testutil.generator.InvasionGenerators._
import joguin.testutil.generator.MainCharacterGenerators._
import org.scalacheck.Gen

object GameProgressGenerators {
  def genGameProgressStart: Gen[GameProgress] =
    for {
      mainCharacter <- genMainCharacter
      invasionList  <- genInvasionSeq(defeated = false)
    } yield GameProgress(mainCharacter, invasionList)
}

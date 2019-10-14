package joguin.testutil.generators

import joguin.game.progress._
import joguin.testutil.generators.InvasionGenerators._
import joguin.testutil.generators.MainCharacterGenerators._
import org.scalacheck.Gen

object GameProgressGenerators {
  def genGameProgressStart: Gen[GameProgress] =
    for {
      mainCharacter <- genMainCharacter
      invasionList  <- genInvasionSeq(defeated = false)
    } yield GameProgress(mainCharacter, invasionList)
}
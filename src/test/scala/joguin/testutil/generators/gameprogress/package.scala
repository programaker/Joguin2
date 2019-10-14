package joguin.testutil.generators

import joguin.game.progress.GameProgress
import joguin.testutil.generators.invasion._
import joguin.testutil.generators.maincharacter._
import org.scalacheck.Gen

package object gameprogress {
  def genGameProgressStart: Gen[GameProgress] =
    for {
      mainCharacter <- genMainCharacter
      invasionList  <- genInvasionSeq(defeated = false)
    } yield GameProgress(mainCharacter, invasionList)
}

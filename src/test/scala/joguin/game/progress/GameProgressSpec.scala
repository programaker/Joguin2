package joguin.game.progress

import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.PropertyBasedSpec
import joguin.testutil.Generators._

class GameProgressSpec extends PropertyBasedSpec {
  property("GameProgress should start with a 0-experience MainCharacter and no defeated Invasions") {
    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion]) =>
      val gp = GameProgress.start(mainCharacter, invasions)

      val invasionNumber = invasions.size
      gp.mainCharacterExperience.value shouldBe 0
      gp.defeatedInvasions.value shouldBe 0
      gp.allInvasionsDefeated shouldBe false
      gp.defeatedInvasionsTrack shouldBe empty
      gp.invasionCount.value shouldBe invasionNumber
      gp.indexedInvasions should have size invasionNumber
    }
  }
}

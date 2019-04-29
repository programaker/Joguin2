package joguin.game.progress

import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.Generators._
import joguin.testutil.PropertyBasedSpec
import org.scalatest.OptionValues._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
class GameProgressSpec extends PropertyBasedSpec {
  property("should start with a 0-experience MainCharacter and no defeated Invasions") {
    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion]) =>
      val gp = GameProgress.start(mainCharacter, invasions)

      val invasionCount = invasions.size
      gp.mainCharacterExperience.value shouldBe 0
      gp.defeatedInvasions.value shouldBe 0
      gp.allInvasionsDefeated shouldBe false
      gp.defeatedInvasionsTrack shouldBe empty
      gp.invasionCount.value shouldBe invasionCount
      gp.indexedInvasions should have size invasionCount
    }
  }

  property("should return an Invasion given a valid index") {
    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion], index: Index) =>
      val gp = GameProgress.start(mainCharacter, invasions)
      val maxIndex = invasions.size

      whenever(index.value <= maxIndex) {
        val invasionAtIndex = invasions(index.value - 1)
        gp.invasionByIndex(index).value shouldBe invasionAtIndex
      }
    }
  }

  property("should return None given an invalid index") {
    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion], index: Index) =>
      val gp = GameProgress.start(mainCharacter, invasions)
      val maxIndex = invasions.size

      whenever(index.value > maxIndex) {
        gp.invasionByIndex(index) shouldBe empty
      }
    }
  }
}

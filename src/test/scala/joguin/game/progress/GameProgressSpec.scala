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

  property("should mark the Invasion at a given index as defeated") {
    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion], index: Index) =>
      val gp = GameProgress.start(mainCharacter, invasions)
      val maxIndex = invasions.size

      whenever(index.value <= maxIndex) {
        val gp1 = gp.defeatInvasion(index)

        gp1.isInvasionDefeated(index) shouldBe true
        gp1.defeatedInvasions.value shouldBe 1
        gp1.defeatedInvasionsTrack should contain(index)
        gp1.allInvasionsDefeated shouldBe false
      }
    }
  }

  property("should increase MainCharacter's experience by a given amount") {
    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion], xp: Experience, n: Int) =>
      val start = GameProgress.start(mainCharacter, invasions)

      val gp1 = (1 to n).foldLeft(start) { (gp, _) =>
        gp.increaseMainCharacterExperience(xp)
      }

      gp1.mainCharacterExperience.value shouldBe (start.mainCharacterExperience.value + (n * xp.value))
    }
  }
}

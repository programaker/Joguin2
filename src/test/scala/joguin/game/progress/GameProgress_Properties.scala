package joguin.game.progress

import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.Generators._
import joguin.testutil.PropertyBasedSpec
import org.scalatest.Inspectors
import org.scalatest.OptionValues._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
class GameProgress_Properties extends PropertyBasedSpec {
  property("start a new GameProgress") {
    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion]) =>
      val gp = GameProgress.start(mainCharacter, invasions)
      val invasionCount = invasions.size

      val shouldNotHappen: Index = 1
      val indexes = (1 to invasionCount).map(refineV[IndexR](_).getOrElse(shouldNotHappen))

      gp.mainCharacterExperience.value shouldBe 0
      gp.allInvasionsDefeated shouldBe false
      Inspectors.forAll(indexes)(idx => gp.isInvasionDefeated(idx) shouldBe false)
    }
  }

  property("get an Invasion by a given Index") {
    forAll(genMainCharacter, genInvasionList, genValidIndex) {
      (mainCharacter: MainCharacter, invasions: List[Invasion], validIndex: Index) =>

        val gp = GameProgress.start(mainCharacter, invasions)
        val invasionAtIndex = invasions(validIndex.value - 1)
        gp.invasionByIndex(validIndex).value shouldBe invasionAtIndex
    }

    forAll(genMainCharacter, genInvasionList, genInvalidIndex) {
      (mainCharacter: MainCharacter, invasions: List[Invasion], invalidIndex: Index) =>

        val gp = GameProgress.start(mainCharacter, invasions)
        gp.invasionByIndex(invalidIndex) shouldBe empty
    }
  }

  property("mark an Invasion as defeated by a given Index") {
    forAll(genMainCharacter, genInvasionList, genValidIndex) {
      (mainCharacter: MainCharacter, invasions: List[Invasion], validIndex: Index) =>

        val gp = GameProgress.start(mainCharacter, invasions)

        val gp1 = gp.defeatInvasion(validIndex)
        gp1.isInvasionDefeated(validIndex) shouldBe true
        gp1.allInvasionsDefeated shouldBe false
    }

    forAll(genMainCharacter, genInvasionList, genInvalidIndex) {
      (mainCharacter: MainCharacter, invasions: List[Invasion], invalidIndex: Index) =>

        val gp = GameProgress.start(mainCharacter, invasions)

        val gp2 = gp.defeatInvasion(invalidIndex)
        gp2.isInvasionDefeated(invalidIndex) shouldBe false
        gp2.allInvasionsDefeated shouldBe false
    }
  }

  property("increase MainCharacter's experience by a given amount") {
    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion], xp: Experience, n: Int) =>
      val start = GameProgress.start(mainCharacter, invasions)

      val gp1 = (1 to n).foldLeft(start) { (gp, _) =>
        gp.increaseMainCharacterExperience(xp)
      }

      gp1.mainCharacterExperience.value shouldBe (start.mainCharacterExperience.value + (n * xp.value))
    }
  }

  property("inform the defeating of all Invasions") {
    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion]) =>
      val start = GameProgress.start(mainCharacter, invasions)
      val invasionCount = invasions.size

      val shouldNotHappen: Index = 1
      val indexes = (1 to invasionCount).map(refineV[IndexR](_).getOrElse(shouldNotHappen))

      val gp1 = indexes.foldLeft(start) { (gp, idx) =>
        gp.defeatInvasion(idx)
      }

      gp1.allInvasionsDefeated shouldBe true
      Inspectors.forAll(indexes)(idx => gp1.isInvasionDefeated(idx) shouldBe true)
    }
  }
}

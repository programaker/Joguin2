package joguin.game.progress

import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.PropertyBasedSpec
import org.scalatest.Inspectors
import org.scalatest.OptionValues._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class GameProgress_Properties extends PropertyBasedSpec {

  property("start gives a GameProgress with MainCharacter's experience = 0 and no defeated invasions") {
    import joguin.testutil.generator.Generators.{invasionList, mainCharacter}

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

  property("getting an Invasion by a valid Index (between 1 and total Invasions), gives Some(Invasion)") {
    import joguin.testutil.generator.Generators.{index, invasionList, mainCharacter}

    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion], validIndex: Index) =>
      val gp = GameProgress.start(mainCharacter, invasions)
      val invasionAtIndex = invasions(validIndex.value - 1)
      gp.invasionByIndex(validIndex).value shouldBe invasionAtIndex
    }
  }

  property("getting an Invasion by an invalid Index (> total Invasions), gives None") {
    import joguin.testutil.generator.Generators.{invalidIndex, invasionList, mainCharacter}

    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion], invalidIndex: Index) =>
      val gp = GameProgress.start(mainCharacter, invasions)
      gp.invasionByIndex(invalidIndex) shouldBe empty
    }
  }

  property("defeating an Invasion by a valid Index (between 1 and total Invasions), marks that Invasion as defeated") {
    import joguin.testutil.generator.Generators.{index, invasionList, mainCharacter}

    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion], validIndex: Index) =>
      val gp = GameProgress.start(mainCharacter, invasions)
      val gp1 = gp.defeatInvasion(validIndex)
      gp1.isInvasionDefeated(validIndex) shouldBe true
    }
  }

  property("defeating an Invasion by an invalid Index (> total Invasions), does nothing") {
    import joguin.testutil.generator.Generators.{invalidIndex, invasionList, mainCharacter}

    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion], invalidIndex: Index) =>
      val gp = GameProgress.start(mainCharacter, invasions)
      val gp1 = gp.defeatInvasion(invalidIndex)
      gp1.isInvasionDefeated(invalidIndex) shouldBe false
    }
  }

  property("increasing MainCharacter's experience by a given amount gives a GameProgress with the increased experience") {
    import joguin.testutil.generator.Generators.{experience, intFrom1, invasionList, mainCharacter}

    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion], xp: Experience, n: Int) =>
      val start = GameProgress.start(mainCharacter, invasions)

      val gp1 = (1 to n).foldLeft(start) { (gp, _) =>
        gp.increaseMainCharacterExperience(xp)
      }

      gp1.mainCharacterExperience.value shouldBe (start.mainCharacterExperience.value + (n * xp.value))
    }
  }

  property("tells if all Invasions were defeated") {
    import joguin.testutil.generator.Generators.{invasionList, mainCharacter}

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

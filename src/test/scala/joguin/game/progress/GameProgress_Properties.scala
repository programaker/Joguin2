package joguin.game.progress

import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.IdxSeq
import joguin.alien.Invasion
import joguin.earth.maincharacter.Experience
import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.PropertyBasedSpec
import org.scalatest.Inspectors
import org.scalatest.OptionValues._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class GameProgress_Properties extends PropertyBasedSpec {

  property("getting an Invasion by a valid Index gives Some(Invasion)") {
    import joguin.testutil.generator.Generators.index
    import joguin.testutil.generator.Generators.invasionSeq
    import joguin.testutil.generator.Generators.mainCharacter

    forAll { (mainCharacter: MainCharacter, invasions: IdxSeq[Invasion], validIndex: Index) =>
      val gp = GameProgress(mainCharacter, invasions)
      val invasionAtIndex = invasions(validIndex - 1)
      invasionByIndex(gp, validIndex).value shouldBe invasionAtIndex
    }
  }

  property("getting an Invasion by an invalid Index gives None") {
    import joguin.testutil.generator.Generators.invalidIndex
    import joguin.testutil.generator.Generators.invasionSeq
    import joguin.testutil.generator.Generators.mainCharacter

    forAll { (mainCharacter: MainCharacter, invasions: IdxSeq[Invasion], invalidIndex: Index) =>
      val gp = GameProgress(mainCharacter, invasions)
      invasionByIndex(gp, invalidIndex) shouldBe empty
    }
  }

  property("defeating an Invasion by a valid Index marks that Invasion as defeated") {
    import joguin.testutil.generator.Generators.index
    import joguin.testutil.generator.Generators.invasionSeq
    import joguin.testutil.generator.Generators.mainCharacter

    forAll { (mainCharacter: MainCharacter, invasions: IdxSeq[Invasion], validIndex: Index) =>
      val gp = GameProgress(mainCharacter, invasions)
      val gp1 = defeatInvasion(gp, validIndex)
      isInvasionDefeated(gp1, validIndex) shouldBe true
    }
  }

  property("defeating an Invasion by an invalid Index does nothing") {
    import joguin.testutil.generator.Generators.invalidIndex
    import joguin.testutil.generator.Generators.invasionSeq
    import joguin.testutil.generator.Generators.mainCharacter

    forAll { (mainCharacter: MainCharacter, invasions: IdxSeq[Invasion], invalidIndex: Index) =>
      val gp = GameProgress(mainCharacter, invasions)
      val gp1 = defeatInvasion(gp, invalidIndex)
      isInvasionDefeated(gp1, invalidIndex) shouldBe false
    }
  }

  property("increasing MainCharacter's experience gives a GameProgress with the increased experience") {
    import joguin.testutil.generator.Generators.experience
    import joguin.testutil.generator.Generators.invasionSeq
    import joguin.testutil.generator.Generators.mainCharacter
    import joguin.testutil.generator.Generators.smallInt

    forAll { (mainCharacter: MainCharacter, invasions: IdxSeq[Invasion], xp: Experience, n: Int) =>
      val start = GameProgress(mainCharacter, invasions)

      val gp1 = (1 to n).foldLeft(start) { (gp, _) =>
        increaseMainCharacterExperience(gp, xp)
      }

      gp1.mainCharacter.experience.value shouldBe (start.mainCharacter.experience.value + (n * xp.value))
    }
  }

  property("tells if all Invasions were defeated") {
    import joguin.testutil.generator.Generators.invasionSeq
    import joguin.testutil.generator.Generators.mainCharacter

    forAll { (mainCharacter: MainCharacter, invasions: IdxSeq[Invasion]) =>
      val start = GameProgress(mainCharacter, invasions)
      val invasionCount = invasions.size
      val indexes = (1 to invasionCount).map(refineV[IndexR](_).getOrElse(1: Index))

      val gp1 = indexes.foldLeft(start) { (gp, idx) =>
        defeatInvasion(gp, idx)
      }

      allInvasionsDefeated(gp1) shouldBe true
      Inspectors.forAll(indexes)(idx => isInvasionDefeated(gp1, idx) shouldBe true)
    }
  }

}

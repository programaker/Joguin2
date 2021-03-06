package joguin.game.progress

import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.IdxSeq
import joguin.alien.invasion.Invasion
import joguin.earth.maincharacter.Experience
import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.PropertyBasedSpec
import joguin.testutil.generators._
import org.scalatest.Inspectors
import org.scalatest.OptionValues._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class GameProgress_Properties extends PropertyBasedSpec {

  property("getting an Invasion by a valid Index gives Some(Invasion)") {
    import index.validIndex
    import invasion.invasionSeq
    import maincharacter.mainCharacter

    forAll { (mainCharacter: MainCharacter, invasions: IdxSeq[Invasion], validIndex: Index) =>
      val gp = GameProgress(mainCharacter, invasions)
      val invasionAtIndex = invasions(validIndex - 1)
      invasionByIndex(gp, validIndex).value shouldBe invasionAtIndex
    }
  }

  property("getting an Invasion by an invalid Index gives None") {
    import index.invalidIndex
    import invasion.invasionSeq
    import maincharacter.mainCharacter

    forAll { (mainCharacter: MainCharacter, invasions: IdxSeq[Invasion], invalidIndex: Index) =>
      val gp = GameProgress(mainCharacter, invasions)
      invasionByIndex(gp, invalidIndex) shouldBe empty
    }
  }

  property("defeating an Invasion by a valid Index marks that Invasion as defeated") {
    import index.validIndex
    import invasion.invasionSeq
    import maincharacter.mainCharacter

    forAll { (mainCharacter: MainCharacter, invasions: IdxSeq[Invasion], validIndex: Index) =>
      val gp = GameProgress(mainCharacter, invasions)
      val gp1 = defeatInvasion(gp, validIndex)
      isInvasionDefeated(gp1, validIndex) shouldBe true
    }
  }

  property("defeating an Invasion by an invalid Index does nothing") {
    import index.invalidIndex
    import invasion.invasionSeq
    import maincharacter.mainCharacter

    forAll { (mainCharacter: MainCharacter, invasions: IdxSeq[Invasion], invalidIndex: Index) =>
      val gp = GameProgress(mainCharacter, invasions)
      val gp1 = defeatInvasion(gp, invalidIndex)
      isInvasionDefeated(gp1, invalidIndex) shouldBe false
    }
  }

  property("increasing MainCharacter's experience gives a GameProgress with the increased experience") {
    import experience.validExperience
    import invasion.invasionSeq
    import maincharacter.mainCharacter
    import other.smallInt

    forAll { (mainCharacter: MainCharacter, invasions: IdxSeq[Invasion], xp: Experience, n: Int) =>
      val start = GameProgress(mainCharacter, invasions)

      val gp1 = (1 to n).foldLeft(start) { (gp, _) =>
        increaseMainCharacterExperience(gp, xp)
      }

      val startXp: Int = ExperienceField.get(start)
      val increasedXp: Int = ExperienceField.get(gp1)
      increasedXp shouldBe (startXp + (n * xp))
    }
  }

  property("tells if all Invasions were defeated") {
    import invasion.invasionSeq
    import maincharacter.mainCharacter

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

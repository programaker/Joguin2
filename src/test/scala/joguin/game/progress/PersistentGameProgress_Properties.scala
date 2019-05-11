package joguin.game.progress

import eu.timepit.refined.auto._
import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.PropertyBasedSpec
import joguin.testutil.generator.Tag
import org.scalacheck.Arbitrary
import org.scalacheck.ScalacheckShapeless._
import org.scalatest.OptionValues._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements", "org.wartremover.warts.Null"))
final class PersistentGameProgress_Properties extends PropertyBasedSpec {

  property("converting a GameProgress into PersistentGameProgress and back, gives the same GameProgress as Option") {
    import joguin.testutil.generator.Generators.invasionList
    import joguin.testutil.generator.Generators.mainCharacter

    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion]) =>
      val gp = GameProgress.start(mainCharacter, invasions)

      val gp1 = gp
        .defeatInvasion(1)
        .increaseMainCharacterExperience(1000)
        .defeatInvasion(2)
        .increaseMainCharacterExperience(500)

      PersistentGameProgress.fromGameProgress(gp).toGameProgress.value shouldBe gp
      PersistentGameProgress.fromGameProgress(gp1).toGameProgress.value shouldBe gp1
    }
  }

  property("converting a PersistentGameProgress with invalid MainCharacter to GameProgress gives None") {
    import Tag._
    import joguin.testutil.generator.ExperienceGenerators._
    import joguin.testutil.generator.Generators.defeatedInvasionsTrack
    import joguin.testutil.generator.Generators.persistentInvasionList
    import joguin.testutil.generator.InvasionGenerators._

    implicitly[Arbitrary[PersistentMainCharacter]]

    //Tag was necessary here to disambiguate Experience and Count implicits,
    //because they are synonyms - both are Refined[Int, NonNegative]
    implicit val x: Arbitrary[Tag[T1, Experience]] = arbTag(genExperience)
    implicit val d: Arbitrary[Tag[T2, Count]] = arbTag(genDefeatedInvasions)

    forAll { (
      mainChar: PersistentMainCharacter,
      xp: Tag[T1, Experience],
      invasions: List[PersistentInvasion],
      defeated: Tag[T2, Count],
      track: List[Index]
    ) =>

      val mainCharIsInvalid = mainChar.name.trim.isEmpty || mainChar.age < 18

      whenever(mainCharIsInvalid) {
        val pgp = PersistentGameProgress(
          mainCharacter = mainChar,
          mainCharacterExperience = xp.value,
          invasions = invasions,
          defeatedInvasions = defeated.value,
          defeatedInvasionsTrack = track.map(_.value)
        )

        pgp.toGameProgress shouldBe empty
      }
    }
  }

  property("converting a PersistentGameProgress with invalid experience to GameProgress gives None") {
    import joguin.testutil.generator.Generators.defeatedInvasionsTrack
    import joguin.testutil.generator.Generators.persistentInvasionList
    import joguin.testutil.generator.Generators.persistentMainCharacter
    import joguin.testutil.generator.Generators.defeatedInvasions
    import joguin.testutil.generator.Generators.invalidExperience

    forAll { (
      mainChar: PersistentMainCharacter,
      invalidXp: Int,
      invasions: List[PersistentInvasion],
      defeated: Count,
      track: List[Index]
    ) =>

      val pgp = PersistentGameProgress(
        mainCharacter = mainChar,
        mainCharacterExperience = invalidXp,
        invasions = invasions,
        defeatedInvasions = defeated.value,
        defeatedInvasionsTrack = track.map(_.value)
      )

      pgp.toGameProgress shouldBe empty
    }
  }

}
package joguin.game.progress

import eu.timepit.refined.auto._
import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter
import joguin.testutil.PropertyBasedSpec
import org.scalacheck.Arbitrary
import org.scalatest.OptionValues._
import org.scalacheck.ScalacheckShapeless._

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

  property("converting a PersistentGameProgress with invalid MainChar. to GameProgress gives None") {
    import joguin.testutil.generator.Generators.defeatedInvasions
    import joguin.testutil.generator.Generators.defeatedInvasionsTrack
    import joguin.testutil.generator.Generators.experience
    import joguin.testutil.generator.Generators.persistentInvasionList
    implicitly[Arbitrary[PersistentMainCharacter]]

    forAll { (
      mainChar: PersistentMainCharacter,
      xp: Experience,
      invasions: List[PersistentInvasion],
      defeated: Int,
      track: List[Int]
    ) =>

      val mainCharIsInvalid = mainChar.name.trim.isEmpty || mainChar.age < 18

      whenever(mainCharIsInvalid) {
        val pgp = PersistentGameProgress(
          mainCharacter = mainChar,
          mainCharacterExperience = xp.value,
          invasions = invasions,
          defeatedInvasions = defeated,
          defeatedInvasionsTrack = track
        )

        pgp.toGameProgress shouldBe empty
      }
    }
  }

}
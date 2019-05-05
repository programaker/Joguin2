package joguin.game.progress

import joguin.testutil.PropertyBasedSpec
import joguin.testutil.Generators._
import org.scalatest.OptionValues._
import joguin.earth.maincharacter.MainCharacter
import joguin.alien.Invasion
import eu.timepit.refined.auto._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class PersistentGameProgress_Properties extends PropertyBasedSpec {
  property("converting a GameProgress into PersistentGameProgress and back, gives the same GameProgress as Option") {
    forAll { (mainCharacter: MainCharacter, invasions: List[Invasion]) =>
      val gp = GameProgress.start(mainCharacter, invasions)

      val gp1 = gp
        .increaseMainCharacterExperience(1000)
        .defeatInvasion(1)
        .defeatInvasion(2)
        .increaseMainCharacterExperience(500)

      PersistentGameProgress.fromGameProgress(gp).toGameProgress.value shouldBe gp
      PersistentGameProgress.fromGameProgress(gp1).toGameProgress.value shouldBe gp1
    }
  }

  property("converting an invalid PersistentGameProgress to GameProgress gives None") {
    forAll(genInvalidPersistentGameProgress) { invalidPgp: PersistentGameProgress =>
      invalidPgp.toGameProgress shouldBe empty
    }
  }
}
package joguin.game.progress

import cats.implicits._
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

  property("converting a GameProgress into PersistentGameProgress and back, gives the same GameProgress as Some") {
    import joguin.testutil.generator.Generators.invasionList
    import joguin.testutil.generator.Generators.mainCharacter

    forAll { (mainCharacter: MainCharacter, invasions: Vector[Invasion]) =>
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

  property("converting an invalid PersistentGameProgress to GameProgress gives None") {
    import Tag.implicits._

    implicitly[Arbitrary[PersistentMainCharacter]]
    implicitly[Arbitrary[Vector[PersistentInvasion]]]
    implicitly[Arbitrary[List[Int]]]

    //Tag[T, A] was necessary here to disambiguate
    //xp and defeatedCount implicits, because both are Ints
    implicitly[Arbitrary[Tag[1, Int]]]
    implicitly[Arbitrary[Tag[2, Int]]]

    forAll {
      (
        mainChar: PersistentMainCharacter,
        xp: Tag[1, Int],
        invasions: Vector[PersistentInvasion],
        defeatedCount: Tag[2, Int],
        defeatedInvasionsTrack: List[Int]
      ) =>
        val invalidStr: String => Boolean = _.trim.isEmpty

        val mainCharIsInvalid = invalidStr(mainChar.name) || mainChar.age < 18
        val xpIsInvalid = xp < 0

        val invasionsAreInvalid = invasions.exists { pi =>
          invalidStr(pi.cityName) || invalidStr(pi.country) || pi.terraformDevicePower <= 0
        }

        val invasionCount = invasions.size
        val defeatedCountIsInvalid = defeatedCount > invasionCount
        val defeatedInvasionsTrackIsInvalid = defeatedInvasionsTrack.lengthCompare(defeatedCount) =!= 0

        val pgpIsInvalid =
          mainCharIsInvalid ||
            xpIsInvalid ||
            invasionsAreInvalid ||
            defeatedCountIsInvalid ||
            defeatedInvasionsTrackIsInvalid

        whenever(pgpIsInvalid) {
          val pgp = PersistentGameProgress(
            mainCharacter = mainChar,
            invasions = invasions,
            defeatedInvasions = defeatedCount,
            defeatedInvasionsTrack = defeatedInvasionsTrack
          )

          pgp.toGameProgress shouldBe empty
        }
    }
  }

}

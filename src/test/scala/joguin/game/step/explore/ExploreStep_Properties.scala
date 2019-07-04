package joguin.game.step.explore

import eu.timepit.refined._
import joguin.earth.city.City
import joguin.game.progress.GameProgress
import joguin.game.progress.Index
import joguin.game.progress.IndexR
import joguin.game.step.Fight
import joguin.game.step.GameOver
import joguin.game.step.Quit
import joguin.testutil.PropertyBasedSpec
import joguin.testutil.generator.Generators
import joguin.testutil.generator.InvasionGenerators
import joguin.testutil.generator.InvasionGenerators._
import joguin.testutil.interpreter.ExploreStepInterpreter
import joguin.testutil.interpreter.ExploreStepInterpreter.ExploreStepF
import joguin.testutil.interpreter.WriteMessageTrack
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.Inside.inside
import org.scalatest.OptionValues._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class ExploreStep_Properties extends PropertyBasedSpec {

  property("displays messages to the player in the correct order") {
    import joguin.testutil.generator.Generators.gameProgressStart
    implicit val i1: Arbitrary[Int] = Arbitrary(Gen.choose(1, invasionListSize))

    forAll { (gp: GameProgress, chosenCity: Int) =>
      val i = gp.invasions.size
      val cities = gp.invasions.map(_.city)
      val firstMessage = 0
      val lastMessage = i + 1

      val answers = Map(
        whereToGo(i) -> List(chosenCity.toString)
      )

      val actualMessages = ExploreStep[ExploreStepF]
        .play(gp)
        .foldMap(exploreStepInterpreter)
        .runS(WriteMessageTrack.build(answers))
        .map(_.indexedMessages)
        .value

      actualMessages.get(firstMessage).value shouldBe "\n"

      cities.foldLeft(1) { (idx, c) =>
        actualMessages.get(idx).value shouldBe invadedCityMessage(idx, c)
        idx + 1
      }

      actualMessages.get(lastMessage).value shouldBe whereToGo(i)
    }
  }

  property("goes to Quit step passing the current progress if the player chooses to quit") {
    import joguin.testutil.generator.Generators.gameProgressStart
    import joguin.testutil.generator.Generators.quitOption

    forAll { (gp: GameProgress, quitOption: String) =>
      val i: Int = gp.invasions.size

      val answers = Map(
        whereToGo(i) -> List(quitOption)
      )

      val nextStep = ExploreStep[ExploreStepF]
        .play(gp)
        .foldMap(exploreStepInterpreter)
        .runA(WriteMessageTrack.build(answers))

      inside(nextStep.value) {
        case Quit(gameProgress) => gameProgress shouldBe gp
      }
    }
  }

  property("repeats a question to the player until receives a valid answer, informing the error") {
    import joguin.testutil.generator.Generators.gameProgressStart
    import joguin.testutil.generator.Generators.smallInt
    import joguin.testutil.generator.Tag
    import joguin.testutil.generator.Tag._
    import joguin.testutil.generator.Tag.implicits._

    implicit val a1: Arbitrary[Tag[T1, String]] = arbTag(genValidOption(InvasionGenerators.invasionListSize))
    implicit val a2: Arbitrary[Tag[T2, String]] = arbTag(Gen.alphaNumStr)

    forAll {
      (
        gp: GameProgress,
        repetitions: Int,
        validOption: Tag[T1, String],
        invalidOption: Tag[T2, String]
      ) =>
        val i: Int = gp.invasions.size
        val options = List.fill[String](repetitions)(invalidOption) ++ List[String](validOption)

        val answers = Map(
          whereToGo(i) -> options
        )

        val actualMessages = ExploreStep[ExploreStepF]
          .play(gp)
          .foldMap(exploreStepInterpreter)
          .runS(WriteMessageTrack.build(answers))
          .map(_.indexedMessages)
          .value

        actualMessages.count { case (_, msg) => msg === whereToGo(i) } shouldBe (repetitions + 1)
        actualMessages.count { case (_, msg) => msg === errorInvalidOption } shouldBe repetitions
    }
  }

  property("tells the player that the mission has been accomplished and the game has ended") {
    import joguin.testutil.generator.Generators.gameProgressStart

    forAll { gp: GameProgress =>
      val cities = gp.invasions.map(_.city)
      val i: Int = gp.invasions.size
      val firstMessage = 0
      val lastMessage = i + 1

      val allInvasionsDefeatedGp = (1 to i).foldLeft(gp) { (progress, idx) =>
        refineV[IndexR](idx)
          .map(progress.defeatInvasion)
          .getOrElse(progress)
      }

      //no player interactions in this scenario
      val answers: Map[String, List[String]] = Map.empty

      val (actualMessages, nextStep) = ExploreStep[ExploreStepF]
        .play(allInvasionsDefeatedGp)
        .foldMap(exploreStepInterpreter)
        .run(WriteMessageTrack.build(answers))
        .map {
          case (track, step) => (track.indexedMessages, step)
        }
        .value

      actualMessages.get(firstMessage).value shouldBe "\n"

      cities.foldLeft(1) { (idx, c) =>
        actualMessages.get(idx).value shouldBe savedCityMessage(idx, c)
        idx + 1
      }

      actualMessages.get(lastMessage).value shouldBe missionAccomplished
      nextStep shouldBe GameOver
    }
  }

  property("goes to the Fight game step passing the current progress if the player chooses a city") {
    import joguin.testutil.generator.Generators.gameProgressStart
    import joguin.testutil.generator.Generators.index

    forAll { (gp: GameProgress, index: Index) =>
      val answers = Map(
        whereToGo(gp.invasions.size) -> List(index.toString)
      )

      val nextStep = ExploreStep[ExploreStepF]
        .play(gp)
        .foldMap(exploreStepInterpreter)
        .runA(WriteMessageTrack.build(answers))

      inside(nextStep.value) {
        case Fight(gameProgress, selectedInvasion) =>
          gameProgress shouldBe gp
          selectedInvasion shouldBe index
      }
    }
  }

  property("going back from Fight with an invasion defeated, displays the corresponding city as saved") {
    import joguin.testutil.generator.Generators.gameProgressStart
    import joguin.testutil.generator.Generators.index

    forAll { (gp: GameProgress, index: Index) =>
      val gp1 = gp.defeatInvasion(index)

      val answers = Map(
        whereToGo(gp.invasions.size) -> List("q")
      )

      val actualMessages = ExploreStep[ExploreStepF]
        .play(gp1)
        .foldMap(exploreStepInterpreter)
        .runS(WriteMessageTrack.build(answers))
        .map(_.indexedMessages)
        .value

      val cities = gp1.invasions.map(_.city)

      cities.foldLeft(1) { (idx, c) =>
        val message = actualMessages.get(idx).value

        if (idx === index.value) {
          message shouldBe savedCityMessage(idx, c)
        } else {
          message shouldBe invadedCityMessage(idx, c)
        }

        idx + 1
      }
    }
  }

  private val exploreStepInterpreter =
    ExploreStepInterpreter.build

  private def invadedCityMessage(index: Int, city: City): String =
    s"$index. \uD83D\uDC7D ${city.name} - ${city.country}\n"

  private def savedCityMessage(index: Int, city: City): String =
    s"$index. \uD83C\uDF0D ${city.name} - ${city.country}\n"

  private def whereToGo(lastCity: Int): String =
    s"\nWhere do you want to go? - (1) to ($lastCity), (Q)uit:\n"

  private val errorInvalidOption =
    "Invalid option\n"

  private val missionAccomplished =
    "\nAll Terraform Devices are destroyed and the Earth is saved!\n" +
      "However, the Zorblaxians are still out there, expanding their galactic empire.\n" +
      "They may return one day, but they must think twice before mess with the Earth again!\n" +
      "\n" + "THE END.\n"

  private def genValidOption(lastCity: Int): Gen[String] =
    Gen.oneOf(
      Generators.genQuitOption,
      Gen.oneOf(1 to lastCity).map(_.toString)
    )

}

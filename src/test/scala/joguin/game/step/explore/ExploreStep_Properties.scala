package joguin.game.step.explore

import eu.timepit.refined.auto._
import joguin.earth.city.City
import joguin.game.progress.GameProgress
import joguin.game.step.Quit
import joguin.testutil.PropertyBasedSpec
import joguin.testutil.interpreter.ExploreStepInterpreter
import joguin.testutil.interpreter.ExploreStepInterpreter.ExploreStepF
import joguin.testutil.interpreter.WriteMessageTrack
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues._
import joguin.testutil.generator.InvasionGenerators._
import org.scalatest.Inside.inside

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class ExploreStep_Properties extends PropertyBasedSpec {

  property("displays messages to the player in the correct order") {
    import joguin.testutil.generator.Generators.gameProgressStart
    implicit val i1: Arbitrary[Int] = Arbitrary(Gen.choose(1, invasionListSize))

    forAll { (gp: GameProgress, chosenCity: Int) =>
      val cities = gp.invasions.map(_.city)
      val cityCount = gp.invasionCount
      val firstMessage = 0
      val lastMessage = cityCount + 1

      val answers = Map(
        whereToGo(cityCount) -> List(chosenCity.toString)
      )

      val actualMessages = ExploreStep[ExploreStepF]
        .play(gp)
        .foldMap(ExploreStepInterpreter.build)
        .runS(WriteMessageTrack.build(answers))
        .map(_.indexedMessages)
        .value

      actualMessages.get(firstMessage).value shouldBe "\n"

      cities.foldLeft(1) { (i, c) =>
        actualMessages.get(i).value shouldBe city(i, c)
        i + 1
      }

      actualMessages.get(lastMessage).value shouldBe whereToGo(cityCount)
    }
  }

  property("goes to Quit step passing the current progress if the player chooses to quit") {
    import joguin.testutil.generator.Generators.gameProgressStart

    forAll { gp: GameProgress =>
      val n: Int = gp.invasionCount

      val answers = Map(
        whereToGo(n) -> List("q")
      )

      val nextStep = ExploreStep[ExploreStepF]
        .play(gp)
        .foldMap(ExploreStepInterpreter.build)
        .runA(WriteMessageTrack.build(answers))

      inside(nextStep.value) {
        case Quit(gameProgress) => gameProgress shouldBe gp
      }
    }
  }

  property("repeats a question to the player until receives a valid answer, informing the error") {
    import joguin.testutil.generator.Generators.gameProgressStart

  }

  private def city(index: Int, city: City): String = s"$index. \uD83D\uDC7D ${city.name} - ${city.country}\n"
  private def savedCity(index: Int, city: City): String = s"$index. \uD83C\uDF0D ${city.name} - ${city.country}\n"
  private def whereToGo(lastCity: Int): String = s"\nWhere do you want to go? - (1) to ($lastCity), (Q)uit:\n"
  private val invalidOption = "Invalid option\n"

  private val missionAccomplished =
    "\nAll Terraform Devices are destroyed and the Earth is saved!\n" +
      "However, the Zorblaxians are still out there, expanding their galactic empire.\n" +
      "They may return one day, but they must think twice before mess with the Earth again!\n" +
      "\n" + "THE END.\n"

}

package joguin.game.step.explore

import joguin.earth.city.City
import joguin.game.progress.GameProgress
import joguin.testutil.PropertyBasedSpec
import joguin.testutil.interpreter.ExploreStepInterpreter
import joguin.testutil.interpreter.ExploreStepInterpreter.ExploreStepF
import joguin.testutil.interpreter.WriteMessageTrack
import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.OptionValues._
import joguin.testutil.generator.InvasionGenerators._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class ExploreStep_Properties extends PropertyBasedSpec {

  property("displays messages to the player in the correct order") {
    import joguin.testutil.generator.Generators.gameProgressStart
    implicit val i1: Arbitrary[Int] = Arbitrary(Gen.choose(1, invasionListSize))

    forAll { (gp: GameProgress, chosenCity: Int) =>
      val cities = gp.invasions.map(_.city)
      val cityCount = cities.size

      val answers = Map(
        whereToGo(cityCount) -> List(chosenCity.toString)
      )

      val actualMessages = ExploreStep[ExploreStepF]
        .play(gp)
        .foldMap(ExploreStepInterpreter.build)
        .runS(WriteMessageTrack.build(answers))
        .map(_.indexedMessages)
        .value

      actualMessages.get(0).value shouldBe "\n"

      cities.foldLeft(1) { (i, c) =>
        actualMessages.get(i).value shouldBe city(i, c)
        i + 1
      }

      actualMessages.get(cityCount + 1).value shouldBe whereToGo(cityCount)
    }
  }

  private def city(index: Int, city: City): String = s"$index. \uD83D\uDC7D ${city.name} - ${city.country}\n"
  private def savedCity(index: Int, city: City): String = s"$index. \uD83C\uDF0D ${city.name} - ${city.country}\n"
  private def whereToGo(lastCity: Int) = s"\nWhere do you want to go? - (1) to ($lastCity), (Q)uit:\n"
  private val invalidOption = "Invalid option\n"

  private val missionAccomplished =
    "\nAll Terraform Devices are destroyed and the Earth is saved!\n" +
      "However, the Zorblaxians are still out there, expanding their galactic empire.\n" +
      "They may return one day, but they must think twice before mess with the Earth again!\n" +
      "\n" + "THE END.\n"

}

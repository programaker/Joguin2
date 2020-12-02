package joguin.game.step.fight

import cats.syntax.show._
import eu.timepit.refined.cats._
import joguin.alien.Power
import joguin.earth.maincharacter.Experience
import joguin.testutil.PropertyBasedSpec
import joguin.game.progress.GameProgress
import joguin.game.progress.Index
import joguin.testutil.generators._
import joguin.testutil.interpreter._
import org.scalacheck.Gen
import org.scalacheck.Arbitrary

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class FightStep_Properties extends PropertyBasedSpec {
  property("player chooses to retreat") {}

  property("player goes to a saved city") {}

  property("player chooses to fight and wins") {}

  property("player chooses to fight and loses") {}

  property("repeats a question until receiving a valid answer") {
    import gameprogress._
    import index.validIndex
    import other.smallInt
    import other.arbitraryTag

    implicit val a1: Arbitrary[Tag[1, String]] = arbitraryTag(genValidOption)
    implicit val a2: Arbitrary[Tag[2, String]] = arbitraryTag(Gen.alphaNumStr)

    forAll {
      (
        gp: GameProgress,
        idx: Index,
        repetitions: Int,
        validOption: Tag[1, String],
        invalidOption: Tag[2, String]
      ) =>
        val options = List.fill[String](repetitions)(invalidOption) ++ List[String](validOption)

        val answers = Map(
          giveOrder -> options
        )

        val actualMessages = new FightStep[MessageInteractionWaitF]
          .play(gp, idx)
          .foldMap(messageInteractionWaitInterpreter)
          .runS(WriteMessageTrack.of(answers))
          .map(_.indexedMessages)
          .value
    }
  }

  private def report(character: String, city: String, devicePower: Power): String =
    show"\nCommander $character, we are getting closer to the Terraform Device of $city.\n" +
      "It is huge! Soon its automatic defense system will detect us and start to attack!\n" +
      show"According to our analysis, the defense power of this Device is $devicePower.\n"

  private val giveOrder = "\nWhat are your orders? - (F)ight, (R)etreat:\n"

  private val animationEarth = "\uD83C\uDF0D"
  private val animationEarthWeapon = "\uD83D\uDE80"

  private val animationAlien = "\uD83D\uDC7D"
  private val animationAlienWeapon = "\u26A1\uFE0F"

  private val animationStrike = "\uD83D\uDCA5"

  private val errorInvalidOption = "Invalid option\n"

  private def earthWon(xp: Experience): String =
    "\nCongratulations! Thanks to your command, our army has destroyed the Terraform Device!\n" +
      "Go ahead and save more cities!\n" +
      show"You have now $xp experience points.\n"

  private def aliensWon(city: String, xp: Experience): String =
    "\nOur army was defeated! This Terraform Device is too powerful!\n" +
      show"Retreat for now and try to save $city another time.\n" +
      show"You have now $xp experience points.\n"

  private def locationAlreadySaved(city: String): String =
    show"\nGood to see $city being rebuilt after the destruction of the Terraform Device!\n" +
      "Life is slowly getting back to normal!\n"

  private def genValidOption: Gen[String] =
    Gen.oneOf("F", "f", "R", "r")
}

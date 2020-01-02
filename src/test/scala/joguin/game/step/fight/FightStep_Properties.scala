package joguin.game.step.fight

import joguin.alien.Power
import joguin.earth.maincharacter.Experience
import joguin.testutil.PropertyBasedSpec

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class FightStep_Properties extends PropertyBasedSpec {

  private def report(character: String, city: String, devicePower: Power) =
    s"\nCommander $character, we are getting closer to the Terraform Device of $city.\n" +
      "It is huge! Soon its automatic defense system will detect us and start to attack!\n" +
      s"According to our analysis, the defense power of this Device is $devicePower.\n"

  private val giveOrder = "\nWhat are your orders? - (F)ight, (R)etreat:\n"

  private val animationEarth = "\uD83C\uDF0D"
  private val animationEarthWeapon = "\uD83D\uDE80"

  private val animationAlien = "\uD83D\uDC7D"
  private val animationAlienWeapon = "\u26A1\uFE0F"

  private val animationStrike = "\uD83D\uDCA5"

  private val errorInvalidOption = "Invalid option\n"

  private def earthWon(xp: Experience) =
    "\nCongratulations! Thanks to your command, our army has destroyed the Terraform Device!\n" +
      "Go ahead and save more cities!\n" +
      s"You have now $xp experience points.\n"

  private def aliensWon(city: String, xp: Experience) =
    "\nOur army was defeated! This Terraform Device is too powerful!\n" +
      s"Retreat for now and try to save $city another time.\n" +
      s"You have now $xp experience points.\n"

  private def locationAlreadySaved(city: String) =
    s"\nGood to see $city being rebuilt after the destruction of the Terraform Device!\n" +
      "Life is slowly getting back to normal!\n"
}

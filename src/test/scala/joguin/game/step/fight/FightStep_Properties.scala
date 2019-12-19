package joguin.game.step.fight

import joguin.testutil.PropertyBasedSpec

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class FightStep_Properties extends PropertyBasedSpec {
  private val report =
    "\nCommander Barbarella, we are getting closer to the Terraform Device of Rio de Janeiro.\n" +
      "It is huge! Soon its automatic defense system will detect us and start to attack!\n" +
      "According to our analysis, the defense power of this Device is 100.\n"

  private val giveOrder = "\nWhat are your orders? - (F)ight, (R)etreat:\n"

  private val animationEarth = "\uD83C\uDF0D"
  private val animationEarthWeapon = "\uD83D\uDE80"

  private val animationAlien = "\uD83D\uDC7D"
  private val animationAlienWeapon = "\u26A1\uFE0F"

  private val animationStrike = "\uD83D\uDCA5"

  private val errorInvalidOption = "Invalid option\n"

  private val earthWon =
    "\nCongratulations! Thanks to your command, our army has destroyed the Terraform Device!\n" +
      "Go ahead and save more cities!\n" +
      "You have now {0} experience points.\n"

  private val aliensWon =
    "\nOur army was defeated! This Terraform Device is too powerful!\n" +
      "Retreat for now and try to save Rio de Janeiro another time.\n" +
      "You have now {0} experience points.\n"

  private val locationAlreadySaved =
    "\nGood to see Rio de Janeiro being rebuilt after the destruction of the Terraform Device!\n" +
      "Life is slowly getting back to normal!\n"
}

package joguin.game.step.explore

import joguin.testutil.PropertyBasedSpec

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class ExploreStep_Properties extends PropertyBasedSpec {
  private val london = "1. \uD83D\uDC7D London - UK\n"
  private val savedLondon = "1. \uD83C\uDF0D London - UK\n"

  private val tokyo = "2. \uD83D\uDC7D Tokyo - Japan\n"
  private val savedTokyo = "2. \uD83C\uDF0D Tokyo - Japan\n"

  private val saoPaulo = "3. \uD83D\uDC7D São Paulo - Brazil\n"
  private val savedSaoPaulo = "3. \uD83C\uDF0D São Paulo - Brazil\n"

  private val whereToGo = "\nWhere do you want to go? - (1) to (3), (Q)uit:\n"
  private val invalidOption = "Invalid option\n"

  private val missionAccomplished =
    "\nAll Terraform Devices are destroyed and the Earth is saved!\n" +
      "However, the Zorblaxians are still out there, expanding their galactic empire.\n" +
      "They may return one day, but they must think twice before mess with the Earth again!\n" +
      "\n" + "THE END.\n"

  property("displays messages to the player in the correct order") {}
}

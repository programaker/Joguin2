package joguin.game.progress

import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.NonNegative
import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter

case class GameProgress(
  mainCharacter: MainCharacter,

  //The experience is not in the MainCharacter to enable
  //the possibility of reuse the same character in a new game,
  //with 0 experience, and at the same time resume a game with
  //the same character, more experienced
  mainCharacterExperience: Experience,

  invasions: List[Invasion],
  defeatedInvasions: Count
) {
  def invasionByIndex(selectedInvasion: Index): Option[Invasion] = {
    //1-based index, to match the invasion list as the player sees it
    //and also the player's input when select an invasion to fight
    Option(invasions(selectedInvasion - 1)) //TODO => O(n)! make it faster
  }

  def increaseMainCharacterExperience(experiencePoints: Experience): GameProgress =
    refineV[NonNegative](mainCharacterExperience.value + experiencePoints.value)
      .map(updatedXp => copy(mainCharacterExperience = updatedXp))
      .getOrElse(this)

  def allInvasionsDefeated: Boolean =
    invasions.lengthCompare(defeatedInvasions) == 0 //TODO => this also could be better

  /*def defeatInvasion(selectedInvasion: Index): GameProgress = {


    this
  }*/
}

object GameProgress {
  def start(mainCharacter: MainCharacter, invasions: List[Invasion]): GameProgress =
    new GameProgress(
      mainCharacter = mainCharacter,
      mainCharacterExperience = 0,
      invasions = invasions,
      defeatedInvasions = 0
    )
}

package joguin.game

import cats.implicits._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.NonNegative
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.refineV
import joguin.alien.Invasion
import joguin.earth.maincharacter.Experience
import joguin.earth.maincharacter.ExperienceR

package object progress {
  type CountR = NonNegative
  type Count = Int Refined CountR

  type IndexR = Positive
  type Index = Int Refined IndexR

  def invasionByIndex(gp: GameProgress, index: Index): Option[Invasion] =
    //1-based index, to match the invasion list as the player sees it
    //and also the player's input when select an invasion to fight
    gp.invasions.get(index - 1)

  def isInvasionDefeated(gp: GameProgress, index: Index): Boolean =
    gp.defeatedInvasionsTrack.contains(index)

  def increaseMainCharacterExperience(gp: GameProgress, experiencePoints: Experience): GameProgress =
    refineV[ExperienceR](gp.mainCharacter.experience.value + experiencePoints.value)
      .map(updatedXp => gp.copy(mainCharacter = gp.mainCharacter.copy(experience = updatedXp))) //TODO => Lenses!
      .getOrElse(gp)

  def allInvasionsDefeated(gp: GameProgress): Boolean =
    gp.invasions.lengthCompare(gp.defeatedInvasions.value) === 0

  def defeatInvasion(gp: GameProgress, index: Index): GameProgress =
    if (index.value > gp.invasions.size) {
      gp
    } else {
      refineV[CountR](gp.defeatedInvasions + 1)
        .map { updatedCount =>
          gp.copy(
            defeatedInvasions = updatedCount,
            defeatedInvasionsTrack = gp.defeatedInvasionsTrack + index
          )
        }
        .getOrElse(gp)
    }
}

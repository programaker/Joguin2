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
    invasionByIndex(gp, index).exists(_.defeated)

  def increaseMainCharacterExperience(gp: GameProgress, experiencePoints: Experience): GameProgress =
    refineV[ExperienceR](gp.mainCharacter.experience + experiencePoints)
      .map(updatedXp => gp.copy(mainCharacter = gp.mainCharacter.copy(experience = updatedXp))) //TODO => Lenses!
      .getOrElse(gp)

  def allInvasionsDefeated(gp: GameProgress): Boolean =
    gp.invasions.forall(_.defeated)

  def defeatInvasion(gp: GameProgress, index: Index): GameProgress =
    invasionByIndex(gp, index)
      .map { invasion =>
        val defeatedInvasion = invasion.copy(defeated = true)
        gp.copy(invasions = gp.invasions.updated(index - 1, defeatedInvasion))
      }
      .getOrElse(gp)
}

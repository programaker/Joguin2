package joguin.game.progress

import cats.implicits._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter

/* TODO => Big GameProgress Refactoring! https://github.com/programaker/Joguin2/issues/4 */
final case class GameProgress(
  mainCharacter: MainCharacter,
  invasions: Vector[Invasion], //TODO => Use some Non-empty data structure or refinement
  defeatedInvasions: Count,
  defeatedInvasionsTrack: Set[Index]
) {
  def invasionByIndex(selectedInvasion: Index): Option[Invasion] =
    //1-based index, to match the invasion list as the player sees it
    //and also the player's input when select an invasion to fight
    invasions.get(selectedInvasion - 1)

  def isInvasionDefeated(selectedInvasion: Index): Boolean =
    defeatedInvasionsTrack.contains(selectedInvasion)

  def increaseMainCharacterExperience(experiencePoints: Experience): GameProgress =
    refineV[ExperienceR](mainCharacter.experience.value + experiencePoints.value)
      .map(updatedXp => copy(mainCharacter = mainCharacter.copy(experience = updatedXp))) //TODO => Lenses!
      .getOrElse(this)

  def allInvasionsDefeated: Boolean =
    invasions.lengthCompare(defeatedInvasions.value) === 0

  def defeatInvasion(selectedInvasion: Index): GameProgress =
    if (selectedInvasion.value > invasions.size) {
      this
    } else {
      refineV[CountR](defeatedInvasions + 1)
        .map { updatedCount =>
          copy(
            defeatedInvasions = updatedCount,
            defeatedInvasionsTrack = defeatedInvasionsTrack + selectedInvasion
          )
        }
        .getOrElse(this)
    }
}

object GameProgress {
  def start(mainCharacter: MainCharacter, invasions: Vector[Invasion]): GameProgress =
    GameProgress(
      mainCharacter = mainCharacter,
      invasions = invasions,
      defeatedInvasions = 0,
      defeatedInvasionsTrack = Set.empty
    )
}

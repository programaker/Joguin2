package joguin.game.progress

import cats.implicits._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin._
import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter

/* TODO => Big GameProgress Refactoring! https://github.com/programaker/Joguin2/issues/4 */
final case class GameProgress(
  //These are the required data
  mainCharacter: MainCharacter,
  //
  //The experience is not in the MainCharacter to enable
  //the possibility of reuse the same character in a new game,
  //with 0 experience, and at the same time resume a game with
  //the same character, more experienced
  mainCharacterExperience: Experience,
  //
  invasions: List[Invasion],
  //
  //
  //These are data derived from invasions for optimisation purposes,
  //like avoiding O(n) operations on an immutable List
  defeatedInvasions: Count,
  defeatedInvasionsTrack: Set[Index],
  indexedInvasions: Map[Index, Invasion],
  invasionCount: Count
) {
  def invasionByIndex(selectedInvasion: Index): Option[Invasion] =
    //1-based index, to match the invasion list as the player sees it
    //and also the player's input when select an invasion to fight
    indexedInvasions.get(selectedInvasion)

  def isInvasionDefeated(selectedInvasion: Index): Boolean =
    defeatedInvasionsTrack.contains(selectedInvasion)

  def increaseMainCharacterExperience(experiencePoints: Experience): GameProgress =
    refineV[ExperienceR](mainCharacterExperience.value + experiencePoints.value)
      .map(updatedXp => copy(mainCharacterExperience = updatedXp))
      .getOrElse(this)

  def allInvasionsDefeated: Boolean =
    defeatedInvasions === invasionCount

  def defeatInvasion(selectedInvasion: Index): GameProgress =
    if (selectedInvasion.value > invasionCount.value) {
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
  def start(mainCharacter: MainCharacter, invasions: List[Invasion]): GameProgress =
    of(
      mainCharacter = mainCharacter,
      mainCharacterExperience = 0,
      invasions = invasions,
      defeatedInvasions = 0,
      defeatedInvasionsTrack = Set.empty
    )

  def of(
    mainCharacter: MainCharacter,
    mainCharacterExperience: Experience,
    invasions: List[Invasion],
    defeatedInvasions: Count,
    defeatedInvasionsTrack: Set[Index]
  ): GameProgress = {
    val zero: (Index, Map[Index, Invasion], Count) = (1, Map.empty, 0)

    val indexedInvasions = invasions.foldLeft(zero) { (tuple, invasion) =>
      val (index, map, count) = tuple

      (refineV[IndexR](index + 1), refineV[CountR](count + 1))
        .mapN { (nextIndex, inc) =>
          (nextIndex, map + (index -> invasion), inc)
        }
        .getOrElse(tuple)
    }

    new GameProgress(
      mainCharacter,
      mainCharacterExperience,
      invasions,
      defeatedInvasions,
      defeatedInvasionsTrack,
      indexedInvasions._2,
      indexedInvasions._3
    )
  }
}

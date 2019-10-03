package joguin.game.progress

import eu.timepit.refined.auto._
import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter

/* TODO => Big GameProgress Refactoring! https://github.com/programaker/Joguin2/issues/4 */
final case class GameProgress(
  mainCharacter: MainCharacter,
  invasions: Vector[Invasion], //TODO => Use some Non-empty data structure or refinement
  defeatedInvasions: Count,
  defeatedInvasionsTrack: Set[Index]
)

object GameProgress {
  def of(mainCharacter: MainCharacter, invasions: Vector[Invasion]): GameProgress =
    GameProgress(
      mainCharacter = mainCharacter,
      invasions = invasions,
      defeatedInvasions = 0,
      defeatedInvasionsTrack = Set.empty
    )
}

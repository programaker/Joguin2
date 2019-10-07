package joguin.game.progress

import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter

/* TODO => Big GameProgress Refactoring! https://github.com/programaker/Joguin2/issues/4 */
final case class GameProgress(
  mainCharacter: MainCharacter,
  invasions: Vector[Invasion] //TODO => Use some Non-empty data structure or refinement
)

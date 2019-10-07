package joguin.game.progress

import joguin.alien.Invasion
import joguin.earth.maincharacter.MainCharacter

final case class GameProgress(
  mainCharacter: MainCharacter,
  invasions: Vector[Invasion] //TODO => Use some Non-empty data structure or refinement
)

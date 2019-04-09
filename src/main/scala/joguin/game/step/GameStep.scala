package joguin.game.step

import joguin.game.progress.{GameProgress, Index}

sealed trait GameStep
case object ShowIntro extends GameStep
case object CreateCharacter extends GameStep
final case class Explore(gameProgress: GameProgress) extends GameStep
final case class Fight(gameProgress: GameProgress, selectedInvasion: Index) extends GameStep
final case class SaveGame(gameProgress: GameProgress) extends GameStep
final case class Quit(gameProgress: GameProgress) extends GameStep
case object GameOver extends GameStep

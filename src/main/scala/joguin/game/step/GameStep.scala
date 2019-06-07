package joguin.game.step

import joguin.game.progress.GameProgress
import joguin.game.progress.Index

sealed abstract class GameStep extends Product with Serializable
case object ShowIntro extends GameStep
case object CreateCharacter extends GameStep
final case class Explore(gameProgress: GameProgress) extends GameStep
final case class Fight(gameProgress: GameProgress, selectedInvasion: Index) extends GameStep
final case class SaveGame(gameProgress: GameProgress) extends GameStep
final case class Quit(gameProgress: GameProgress) extends GameStep
case object GameOver extends GameStep

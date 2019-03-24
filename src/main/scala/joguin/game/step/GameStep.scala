package joguin.game.step

import cats.InjectK
import cats.free.Free
import cats.free.Free._
import joguin.game.progress.{GameProgress, Index}
import joguin.game.step.GameStepOps.NextGameStep

sealed trait GameStepF[A]
case object ShowIntro extends GameStepF[NextGameStep]
case object CreateCharacter extends GameStepF[NextGameStep]
final case class Explore(gameProgress: GameProgress) extends GameStepF[NextGameStep]
final case class Fight(gameProgress: GameProgress, selectedInvasion: Index) extends GameStepF[NextGameStep]
final case class SaveGame(gameProgress: GameProgress) extends GameStepF[NextGameStep]
final case class Quit(gameProgress: GameProgress) extends GameStepF[NextGameStep]
case object GameOver extends GameStepF[NextGameStep]

final class GameStepOps[G[_]](implicit I: InjectK[GameStepF, G]) {
  def showIntro: Free[G, NextGameStep] =
    inject[GameStepF, G](ShowIntro)

  def createCharacter: Free[G, NextGameStep] =
    inject[GameStepF, G](CreateCharacter)

  def explore(gameProgress: GameProgress): Free[G, NextGameStep] =
    inject[GameStepF, G](Explore(gameProgress))

  def fight(gameProgress: GameProgress, selectedInvasion: Index): Free[G, NextGameStep] =
    inject[GameStepF, G](Fight(gameProgress, selectedInvasion))

  def saveGame(gameProgress: GameProgress): Free[G, NextGameStep] =
    inject[GameStepF, G](SaveGame(gameProgress))

  def quit(gameProgress: GameProgress): Free[G, NextGameStep] =
    inject[GameStepF, G](Quit(gameProgress))

  def gameOver: Free[G, NextGameStep] =
    inject[GameStepF, G](GameOver)
}
object GameStepOps {
  type NextGameStep = GameStepF[_]
  implicit def create[G[_]](implicit I: InjectK[GameStepF, G]): GameStepOps[G] = new GameStepOps[G]
}

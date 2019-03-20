package joguin.game.step

import cats.InjectK
import cats.free.Free
import cats.free.Free._
import joguin.game.progress.{GameProgress, Index}


final case class NextGameStep(gameProgress: GameProgress, next: GameStepOp[_])


sealed trait GameStepOp[A]
case object ShowIntro extends GameStepOp[NextGameStep]
case object CreateCharacter extends GameStepOp[NextGameStep]
final case class Explore(gameProgress: GameProgress) extends GameStepOp[NextGameStep]
final case class Fight(gameProgress: GameProgress, selectedInvasion: Index) extends GameStepOp[NextGameStep]
final case class SaveGame(gameProgress: GameProgress) extends GameStepOp[NextGameStep]
final case class Quit(gameProgress: GameProgress) extends GameStepOp[NextGameStep]
case object GameOver extends GameStepOp[NextGameStep]


final class GameStep[F[_]](implicit I: InjectK[GameStepOp,F]) {
  def showIntro: Free[F,NextGameStep] =
    inject[GameStepOp,F](ShowIntro)

  def createCharacter: Free[F,NextGameStep] =
    inject[GameStepOp,F](CreateCharacter)

  def explore(gameProgress: GameProgress): Free[F,NextGameStep] =
    inject[GameStepOp,F](Explore(gameProgress))

  def fight(gameProgress: GameProgress, selectedInvasion: Index): Free[F,NextGameStep] =
    inject[GameStepOp,F](Fight(gameProgress, selectedInvasion))

  def saveGame(gameProgress: GameProgress): Free[F,NextGameStep] =
    inject[GameStepOp,F](SaveGame(gameProgress))

  def quit(gameProgress: GameProgress): Free[F,NextGameStep] =
    inject[GameStepOp,F](Quit(gameProgress))

  def gameOver: Free[F,NextGameStep] =
    inject[GameStepOp,F](GameOver)
}

object GameStep {
  implicit def create[F[_]](implicit I: InjectK[GameStepOp,F]): GameStep[F] = new GameStep[F]
}

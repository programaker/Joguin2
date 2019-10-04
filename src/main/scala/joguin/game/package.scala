package joguin

import java.io.File

import cats.data.EitherK
import cats.free.Free
import cats.free.Free.pure
import cats.~>
import joguin.alien.terraformdevice.PowerGeneratorF
import joguin.alien.terraformdevice.PowerGeneratorInterpreter
import joguin.earth.city.CityRepositoryF
import joguin.earth.city.CityRepositoryInterpreter
import joguin.game.progress.GameProgressRepositoryF
import joguin.game.progress.GameProgressRepositoryInterpreter
import joguin.game.step.CreateCharacter
import joguin.game.step.Explore
import joguin.game.step.Fight
import joguin.game.step.GameOver
import joguin.game.step.GameStep
import joguin.game.step.Quit
import joguin.game.step.SaveGame
import joguin.game.step.ShowIntro
import joguin.game.step.createcharacter._
import joguin.game.step.explore.ExploreStep
import joguin.game.step.fight.FightStep
import joguin.game.step.quit.playQuitStep
import joguin.game.step.savegame.playSaveGameStep
import joguin.game.step.showintro._
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.interaction.InteractionInterpreter
import joguin.playerinteraction.message.MessageSourceF
import joguin.playerinteraction.message.MessageSourceInterpreter
import joguin.playerinteraction.message.MessagesF
import joguin.playerinteraction.message.MessagesInterpreter
import joguin.playerinteraction.wait.WaitF
import joguin.playerinteraction.wait.WaitInterpreter

package object game {
  def playGame: Free[GameF, Unit] = gameLoopFn()(ShowIntro)

  //Starting from the "F2" alias, new algebras must be "prepended"
  //to the new Coproduct, to match the InjectK[MyAlgebraF, C] declaration,
  //otherwise, the implicit InjectK instances won't be provided
  type F1[A] = EitherK[MessagesF, MessageSourceF, A]
  type F2[A] = EitherK[InteractionF, F1, A]
  type F3[A] = EitherK[CityRepositoryF, F2, A]
  type F4[A] = EitherK[GameProgressRepositoryF, F3, A]
  type F5[A] = EitherK[PowerGeneratorF, F4, A]
  type GameF[A] = EitherK[WaitF, F5, A]

  /** GameF composite interpreter to any F */
  def gameInterpreter[F[_]: Recovery: Lazy](saveProgressFile: String): GameF ~> F = {
    //The interpreter composition was written this way (with variables)
    //to match the Coproduct composition (see the types above) and
    //make the order easier to see.
    //
    //This is important, as the interpreter composition must be
    //in the same order of the Coproduct composition and, without the
    //variables, it would be "upside-down" in relation to the Coproduct
    val i1 = new MessagesInterpreter[F] or new MessageSourceInterpreter[F]
    val i2 = new InteractionInterpreter[F] or i1
    val i3 = new CityRepositoryInterpreter[F] or i2
    val i4 = new GameProgressRepositoryInterpreter[F](new File(saveProgressFile)) or i3
    val i5 = new PowerGeneratorInterpreter[F] or i4
    val iGame = new WaitInterpreter[F] or i5

    iGame
  }

  private def gameLoopFn(): GameStep => Free[GameF, Unit] = {
    val explore = new ExploreStep[GameF]
    val fight = new FightStep[GameF]

    def gameLoop(step: GameStep): Free[GameF, Unit] = step match {
      case ShowIntro =>
        playShowIntroStep[GameF].flatMap(gameLoop)

      case CreateCharacter =>
        playCreateCharacterStep[GameF].flatMap(gameLoop)

      case Explore(gameProgress) =>
        explore.play(gameProgress).flatMap(gameLoop)

      case Fight(gameProgress, selectedInvasion) =>
        fight.play(gameProgress, selectedInvasion).flatMap(gameLoop)

      case SaveGame(gameProgress) =>
        playSaveGameStep[GameF](gameProgress).flatMap(gameLoop)

      case Quit(gameProgress) =>
        playQuitStep[GameF](gameProgress).flatMap(gameLoop)

      case GameOver =>
        pure(())
    }

    gameLoop
  }
}

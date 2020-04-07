package joguin.game.step

import cats.free.Free
import eu.timepit.refined.refineV
import eu.timepit.refined.string.MatchesRegex
import joguin.game.progress.GameProgress
import joguin.game.step.GameStep.GameOver
import joguin.game.step.GameStep.SaveGame
import joguin.playerinteraction.interaction._
import joguin.playerinteraction.message.MessageSource.QuitMessageSource
import joguin.playerinteraction.message.MessageSource.QuitMessageSource._

package object quit {
  type QuitOptionR = MatchesRegex["^[yn]$"]

  def playQuitStep[F[_]](gameProgress: GameProgress)(implicit env: QuitStepEnv[F]): Free[F, GameStep] = {
    import env._
    import messageOps._

    val answer = for {
      src            <- getLocalizedMessageSource(QuitMessageSource)
      wantToSaveGame <- getMessage(src)(want_to_save_game)
      invalidOption  <- getMessage(src)(error_invalid_option)
      answer         <- ask(wantToSaveGame, invalidOption, parseQuitOption)
    } yield answer

    answer.map {
      case Yes => SaveGame(gameProgress)
      case No  => GameOver
    }
  }

  private def parseQuitOption(s: String): Option[QuitOption] =
    refineV[QuitOptionR](s.toLowerCase).toOption.map(_.value match {
      case "y" => Yes
      case "n" => No
    })
}

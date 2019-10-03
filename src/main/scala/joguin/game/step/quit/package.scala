package joguin.game.step

import cats.free.Free
import eu.timepit.refined.W
import eu.timepit.refined.refineV
import eu.timepit.refined.string.MatchesRegex
import joguin.game.progress.GameProgress
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.interaction._
import joguin.playerinteraction.message.MessageSourceOps
import joguin.playerinteraction.message.MessagesOps
import joguin.playerinteraction.message.QuitMessageSource

package object quit {
  type QuitOptionR = MatchesRegex[W.`"""^[yn]$"""`.T]

  def playQuitStep[F[_]](gameProgress: GameProgress)(
    implicit
    i: InteractionOps[F],
    m: MessagesOps[F],
    s: MessageSourceOps[F]
  ): Free[F, GameStep] = {
    import QuitMessageSource._
    import m._
    import s._

    val messageSource = getLocalizedMessageSource(QuitMessageSource)

    val answer = for {
      src            <- messageSource
      wantToSaveGame <- getMessage(src)(want_to_save_game)
      invalidOption  <- getMessage(src)(error_invalid_option)
      answer         <- ask(wantToSaveGame, invalidOption, parseQuitOption)
    } yield answer

    answer.map {
      case Yes => SaveGame(gameProgress)
      case No  => GameOver
    }
  }

  def parseQuitOption(s: String): Option[QuitOption] =
    refineV[QuitOptionR](s.toLowerCase).toOption.map(_.value match {
      case "y" => Yes
      case "n" => No
    })
}

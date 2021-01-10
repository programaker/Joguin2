package joguin.game.step.showintro

sealed trait ShowIntroOption

object ShowIntroOption {
  case object NewGame extends ShowIntroOption
  case object RestoreGame extends ShowIntroOption
  case object QuitGame extends ShowIntroOption
}

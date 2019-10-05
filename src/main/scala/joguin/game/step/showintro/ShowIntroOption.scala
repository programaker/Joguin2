package joguin.game.step.showintro

sealed abstract class ShowIntroOption extends Product with Serializable

object ShowIntroOption {
  case object NewGame extends ShowIntroOption
  case object RestoreGame extends ShowIntroOption
  case object QuitGame extends ShowIntroOption
}

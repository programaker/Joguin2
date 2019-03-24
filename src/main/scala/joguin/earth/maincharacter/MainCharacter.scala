package joguin.earth.maincharacter

import joguin.Name

final case class MainCharacter(
  name: Name,
  gender: Gender,
  age: Age
)

sealed trait Gender
object Female extends Gender
object Male extends Gender
object Other extends Gender

object Gender {
  def byCode(code: String): Option[Gender] =
    code.toLowerCase match {
      case "f" => Some(Female)
      case "m" => Some(Male)
      case "o" => Some(Other)
      case _   => None
    }
}

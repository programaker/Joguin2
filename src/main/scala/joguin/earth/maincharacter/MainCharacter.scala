package joguin.earth.maincharacter

import joguin.Name
import joguin.game.progress.Experience

final case class MainCharacter(
  name: Name,
  gender: Gender,
  age: Age,
  experience: Experience
)

sealed abstract class Gender(val code: String) extends Product with Serializable
case object Female extends Gender("f")
case object Male extends Gender("m")
case object Other extends Gender("o")

object Gender {
  def byCode(code: String): Option[Gender] =
    code.toLowerCase match {
      case Female.code => Some(Female)
      case Male.code   => Some(Male)
      case Other.code  => Some(Other)
      case _           => None
    }
}

package joguin.earth.maincharacter

import joguin.Name

final case class MainCharacter(
  name: Name,
  gender: Gender,
  age: Age
)

sealed abstract class Gender(val code: String)
object Female extends Gender("f")
object Male extends Gender("m")
object Other extends Gender("o")

object Gender {
  def byCode(code: String): Option[Gender] =
    code.toLowerCase match {
      case Female.code => Some(Female)
      case Male.code   => Some(Male)
      case Other.code  => Some(Other)
      case _           => None
    }
}

package joguin.earth.maincharacter

import joguin.Name

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

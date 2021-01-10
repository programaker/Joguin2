package joguin.earth.maincharacter

import joguin.Name

final case class MainCharacter(
  name: Name,
  gender: Gender,
  age: Age,
  experience: Experience
)

sealed trait Gender
object Gender {
  case object Female extends Gender
  case object Male extends Gender
  case object Other extends Gender
}

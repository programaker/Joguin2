package joguin.earth.maincharacter

import joguin.Name
import joguin.earth.maincharacter.MainCharacter.Gender

case class MainCharacter(
  name: Name,
  gender: Gender,
  age: Age
)

object MainCharacter {
  sealed trait Gender
  object Female extends Gender
  object Male extends Gender
  object Other extends Gender
}



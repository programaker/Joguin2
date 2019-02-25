package joguin.earth

import joguin.earth.MainCharacter.Gender
import joguin.{Age, Name}

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



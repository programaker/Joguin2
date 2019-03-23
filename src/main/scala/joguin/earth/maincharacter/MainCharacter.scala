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


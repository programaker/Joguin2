package joguin

import joguin.earth.maincharacter.Gender
import joguin.earth.maincharacter.Gender.Female
import joguin.earth.maincharacter.Gender.Male
import joguin.earth.maincharacter.Gender.Other

package object testutil {
  def genderToCode(gender: Gender): String =
    gender match {
      case Female => "f"
      case Male   => "m"
      case Other  => "o"
    }
}

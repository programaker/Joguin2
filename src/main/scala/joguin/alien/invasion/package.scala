package joguin.alien

import joguin.Name
import monocle.Lens
import monocle.macros.GenLens

package object invasion {
  val DefensePowerField: Lens[Invasion, Power] = GenLens[Invasion](_.terraformDevice.defensePower)
  val CityNameField: Lens[Invasion, Name] = GenLens[Invasion](_.city.name)
  val CountryField: Lens[Invasion, Name] = GenLens[Invasion](_.city.country)
}

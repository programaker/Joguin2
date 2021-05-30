package joguin.game.progress.json

import joguin.IdxSeq

final case class GameProgressDto(
  mainCharacter: MainCharacterDto,
  invasions: IdxSeq[InvasionDto]
)

final case class MainCharacterDto(
  name: String,
  gender: String,
  age: Int,
  experience: Int
)

final case class InvasionDto(
  terraformDevice: TerraformDeviceDto,
  city: CityDto,
  defeated: Boolean
)

final case class TerraformDeviceDto(defensePower: Int)

final case class CityDto(name: String, country: String)

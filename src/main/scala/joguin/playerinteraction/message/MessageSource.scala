package joguin.playerinteraction.message

import java.util.Locale

import cats.InjectK
import cats.free.Free
import cats.free.Free._

sealed abstract class MessageSource extends Product with Serializable {
  type Key
}

case object CreateCharacterMessageSource extends MessageSource {
  type Key = CreateCharacterKey

  sealed abstract class CreateCharacterKey extends Product with Serializable
  case object create_character extends CreateCharacterKey
  case object inform_character_name extends CreateCharacterKey
  case object error_invalid_name extends CreateCharacterKey
  case object inform_character_gender extends CreateCharacterKey
  case object error_invalid_gender extends CreateCharacterKey
  case object inform_character_age extends CreateCharacterKey
  case object error_invalid_age extends CreateCharacterKey
  case object character_created extends CreateCharacterKey
}

case object ExploreMessageSource extends MessageSource {
  type Key = ExploreKey

  sealed abstract class ExploreKey extends Product with Serializable
  case object human_dominated_city extends ExploreKey
  case object alien_dominated_city extends ExploreKey
  case object mission_accomplished extends ExploreKey
  case object where_do_you_want_to_go extends ExploreKey
  case object error_invalid_option extends ExploreKey
}

case object QuitMessageSource extends MessageSource {
  type Key = QuitKey

  sealed abstract class QuitKey extends Product with Serializable
  case object want_to_save_game extends QuitKey
  case object error_invalid_option extends QuitKey
}

case object ShowIntroMessageSource extends MessageSource {
  type Key = ShowIntroKey

  sealed abstract class ShowIntroKey extends Product with Serializable
  case object intro extends ShowIntroKey
  case object start_with_resume extends ShowIntroKey
  case object start_no_resume extends ShowIntroKey
  case object error_invalid_option extends ShowIntroKey
  case object welcome_back extends ShowIntroKey
}

case object FightMessageSource extends MessageSource {
  type Key = FightKey

  sealed abstract class FightKey extends Product with Serializable
  case object city_already_saved extends FightKey
  case object report extends FightKey
  case object give_order extends FightKey
  case object error_invalid_option extends FightKey
  case object earth_won extends FightKey
  case object aliens_won extends FightKey
  case object animation_earth extends FightKey
  case object animation_earth_weapon extends FightKey
  case object animation_alien extends FightKey
  case object animation_alien_weapon extends FightKey
  case object animation_strike extends FightKey
}

case object SaveGameMessageSource extends MessageSource {
  type Key = SaveGameKey

  sealed abstract class SaveGameKey extends Product with Serializable
  case object save_game_success extends SaveGameKey
  case object save_game_error extends SaveGameKey
}

final case class LocalizedMessageSource[T <: MessageSource](source: T, locale: Locale)

sealed abstract class MessageSourceF[A] extends Product with Serializable

final case class GetLocalizedMessageSource[T <: MessageSource](source: T)
    extends MessageSourceF[LocalizedMessageSource[T]]

final class MessageSourceOps[C[_]](implicit i: InjectK[MessageSourceF, C]) {
  def getLocalizedMessageSource[T <: MessageSource](source: T): Free[C, LocalizedMessageSource[T]] =
    inject[MessageSourceF, C](GetLocalizedMessageSource(source))
}

object MessageSourceOps {
  implicit def messageSourceOps[C[_]](implicit i: InjectK[MessageSourceF, C]): MessageSourceOps[C] =
    new MessageSourceOps[C]
}

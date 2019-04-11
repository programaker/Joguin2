package joguin.playerinteraction.message

import java.util.Locale

import cats.InjectK
import cats.free.Free
import cats.free.Free._


sealed trait MessageSource {
  type Key
}

case object CreateCharacterMessageSource extends MessageSource {
  type Key = CreateCharacterKey

  sealed trait CreateCharacterKey
  object create_character extends CreateCharacterKey
  object inform_character_name extends CreateCharacterKey
  object error_invalid_name extends CreateCharacterKey
  object inform_character_gender extends CreateCharacterKey
  object error_invalid_gender extends CreateCharacterKey
  object inform_character_age extends CreateCharacterKey
  object error_invalid_age extends CreateCharacterKey
  object character_created extends CreateCharacterKey
}

case object ExploreMessageSource extends MessageSource {
  type Key = ExploreKey

  sealed trait ExploreKey
  object human_dominated_city extends ExploreKey
  object alien_dominated_city extends ExploreKey
  object mission_accomplished extends ExploreKey
  object where_do_you_want_to_go extends ExploreKey
  object error_invalid_option extends ExploreKey
}

case object QuitMessageSource extends MessageSource {
  type Key = QuitKey

  sealed trait QuitKey
  object want_to_save_game extends QuitKey
  object error_invalid_option extends QuitKey
}

case object ShowIntroMessageSource extends MessageSource {
  type Key = ShowIntroKey

  sealed trait ShowIntroKey
  object intro extends ShowIntroKey
  object start_with_resume extends ShowIntroKey
  object start_no_resume extends ShowIntroKey
  object error_invalid_option extends ShowIntroKey
  object welcome_back extends ShowIntroKey
}

case object FightMessageSource extends MessageSource {
  type Key = FightKey

  sealed trait FightKey
  object city_already_saved extends FightKey
  object report extends FightKey
  object give_order extends FightKey
  object error_invalid_option extends FightKey
  object earth_won extends FightKey
  object aliens_won extends FightKey
  object animation_earth extends FightKey
  object animation_earth_weapon extends FightKey
  object animation_alien extends FightKey
  object animation_alien_weapon extends FightKey
  object animation_strike extends FightKey
}

case object SaveGameMessageSource extends MessageSource {
  type Key = SaveGameKey

  sealed trait SaveGameKey
  object save_game_success extends SaveGameKey
  object save_game_error extends SaveGameKey
}

final case class LocalizedMessageSource[T <: MessageSource](source: T, locale: Locale)


sealed trait MessageSourceF[A]

final case class GetLocalizedMessageSource[T <: MessageSource](source: T)
  extends MessageSourceF[LocalizedMessageSource[T]]


final class MessageSourceOps[G[_]](implicit i: InjectK[MessageSourceF, G]) {
  def getLocalizedMessageSource[T <: MessageSource](source: T): Free[G, LocalizedMessageSource[T]] =
    inject[MessageSourceF, G](GetLocalizedMessageSource(source))
}
object MessageSourceOps {
  implicit def create[G[_]](implicit i: InjectK[MessageSourceF, G]): MessageSourceOps[G] =
    new MessageSourceOps[G]
}

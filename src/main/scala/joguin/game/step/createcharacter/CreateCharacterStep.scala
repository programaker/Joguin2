package joguin.game.step.createcharacter

import cats.free.Free
import cats.free.Free._
import cats.implicits._
import eu.timepit.refined._
import eu.timepit.refined.string.ValidInt
import joguin.alien.AlienArmy
import joguin.alien.terraformdevice.PowerGeneratorOps
import joguin.earth.city.CityRepositoryOps
import joguin.earth.maincharacter.{Age, Gender, MainCharacter, Major}
import joguin.game.progress.GameProgress
import joguin.game.step.{Explore, GameStep}
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.{CreateCharacterMessageSource, MessageSourceOps, MessagesOps}
import joguin.{Name, NonBlankString}

final class CreateCharacterStep(
  implicit s: MessageSourceOps[CreateCharacterF],
  m: MessagesOps[CreateCharacterF],
  i: InteractionOps[CreateCharacterF],
  c: CityRepositoryOps[CreateCharacterF],
  p: PowerGeneratorOps[CreateCharacterF]
) {
  import c._
  import i._
  import m._
  import s._
  import CreateCharacterMessageSource._

  def start: Free[CreateCharacterF, GameStep] =
    for {
      src <- getLocalizedMessageSource(CreateCharacterMessageSource)
      message <- pure(getMessage(src)(_))
      messageFmt <- pure(getMessageFmt(src)(_, _))

      createCharacterMessage <- message(create_character)
      _ <- writeMessage(createCharacterMessage)

      informName <- message(inform_character_name)
      informNameError <- message(error_invalid_name)
      name: Name <- ask(informName, informNameError, parseName)

      informGender <- message(inform_character_gender)
      informGenderError <- message(error_invalid_gender)
      gender: Gender <- ask(informGender, informGenderError, parseGender)

      informAge <- message(inform_character_age)
      informAgeError <- message(error_invalid_age)
      age: Age <- ask(informAge, informAgeError, parseAge)

      mc <- pure(MainCharacter(name, gender, age))

      characterCreated <- messageFmt(character_created, List(name.value))
      _ <- writeMessage(characterCreated)

      gameProgress <- initGameProgress(mc)
    } yield Explore(gameProgress)

  private def parseName(name: String): Option[Name] =
    refineV[NonBlankString](name).toOption

  private def parseGender(gender: String): Option[Gender] =
    Gender.byCode(gender)

  private def parseAge(age: String): Option[Age] =
    refineV[ValidInt](age)
      .map(_.value.toInt)
      .flatMap(refineV[Major](_))
      .toOption

  private def initGameProgress(mainCharacter: MainCharacter): Free[CreateCharacterF, GameProgress] =
    findAllCities
      .flatMap(_.map(city => AlienArmy.attack(city)).sequence)
      .map(GameProgress.start(mainCharacter, _))
}

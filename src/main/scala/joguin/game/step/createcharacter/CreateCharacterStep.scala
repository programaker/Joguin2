package joguin.game.step.createcharacter

import cats.free.Free
import cats.free.Free._
import cats.implicits._
import eu.timepit.refined.auto._
import joguin.alien._
import joguin.alien.terraformdevice.PowerGeneratorOps
import joguin.earth.city.CityRepositoryOps
import joguin.earth.maincharacter.Gender
import joguin.earth.maincharacter.MainCharacter
import joguin.earth.maincharacter.parseAge
import joguin.game.progress.GameProgress
import joguin.game.step.Explore
import joguin.game.step.GameStep
import joguin.parseName
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.CreateCharacterMessageSource
import joguin.playerinteraction.message.MessageSourceOps
import joguin.playerinteraction.message.MessagesOps

final class CreateCharacterStep[F[_]](
  implicit
  s: MessageSourceOps[F],
  m: MessagesOps[F],
  i: InteractionOps[F],
  c: CityRepositoryOps[F],
  p: PowerGeneratorOps[F]
) {
  import CreateCharacterMessageSource._
  import c._
  import i._
  import m._
  import s._

  def play: Free[F, GameStep] =
    for {
      src        <- getLocalizedMessageSource(CreateCharacterMessageSource)
      message    <- pure(getMessage(src)(_))
      messageFmt <- pure(getMessageFmt(src)(_, _))

      createCharacterMessage <- message(create_character)
      _                      <- writeMessage(createCharacterMessage)

      informName      <- message(inform_character_name)
      informNameError <- message(error_invalid_name)
      name            <- ask(informName, informNameError, parseName)

      informGender      <- message(inform_character_gender)
      informGenderError <- message(error_invalid_gender)
      gender            <- ask(informGender, informGenderError, Gender.parseGender)

      informAge      <- message(inform_character_age)
      informAgeError <- message(error_invalid_age)
      age            <- ask(informAge, informAgeError, parseAge)

      mc <- pure(MainCharacter(name, gender, age, experience = 0))

      characterCreated <- messageFmt(character_created, List(name.value))
      _                <- writeMessage(characterCreated)

      gameProgress <- initGameProgress(mc)
    } yield Explore(gameProgress)

  private def initGameProgress(mainCharacter: MainCharacter): Free[F, GameProgress] =
    findAllCities
      .map(_.map(invadeCity(_, MinPower, MaxPower)))
      .flatMap(_.sequence)
      .map(GameProgress.start(mainCharacter, _))
}

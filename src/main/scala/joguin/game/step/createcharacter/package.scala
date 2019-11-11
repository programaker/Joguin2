package joguin.game.step

import cats.free.Free
import cats.free.Free._
import cats.implicits._
import eu.timepit.refined.auto._
import joguin.alien.MaxPower
import joguin.alien.MinPower
import joguin.alien.invadeCity
import joguin.earth.maincharacter.MainCharacter
import joguin.earth.maincharacter.parseAge
import joguin.earth.maincharacter.parseGender
import joguin.game.progress.GameProgress
import joguin.game.step.GameStep._
import joguin.parseName
import joguin.playerinteraction.interaction.ask
import joguin.playerinteraction.message.MessageSource.CreateCharacterMessageSource
import joguin.playerinteraction.message.MessageSource.CreateCharacterMessageSource._

package object createcharacter {
  def playCreateCharacterStep[F[_]](implicit env: CreateCharacterStepEnv[F]): Free[F, GameStep] = {
    import env._
    import interactionOps._
    import messageOps._

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
      gender            <- ask(informGender, informGenderError, parseGender)

      informAge      <- message(inform_character_age)
      informAgeError <- message(error_invalid_age)
      age            <- ask(informAge, informAgeError, parseAge)

      mc <- pure(MainCharacter(name, gender, age, experience = 0))

      characterCreated <- messageFmt(character_created, List(name.value))
      _                <- writeMessage(characterCreated)

      gameProgress <- initialGameProgress(mc)
    } yield Explore(gameProgress)
  }

  private def initialGameProgress[F[_]](
    mainCharacter: MainCharacter
  )(implicit env: CreateCharacterStepEnv[F]): Free[F, GameProgress] = {
    import env._

    cityRepositoryOps.findAllCities
      .map(_.map(invadeCity(_, MinPower, MaxPower)))
      .flatMap(_.sequence)
      .map(GameProgress(mainCharacter, _))
  }
}

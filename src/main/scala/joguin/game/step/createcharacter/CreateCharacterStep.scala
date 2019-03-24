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
import joguin.game.step.Explore
import joguin.game.step.GameStepOps.NextGameStep
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.{CreateCharacterMessageSource, MessageSourceOps, MessagesOps}
import joguin.{Name, NonBlankString}

class CreateCharacterStep(
  implicit S: MessageSourceOps[CreateCharacterF],
  M: MessagesOps[CreateCharacterF],
  I: InteractionOps[CreateCharacterF],
  C: CityRepositoryOps[CreateCharacterF],
  P: PowerGeneratorOps[CreateCharacterF]
) {
  import C._
  import I._
  import InteractionOps._
  import M._
  import S._

  def start: Free[CreateCharacterF, NextGameStep] =
    for {
      src <- getLocalizedMessageSource(CreateCharacterMessageSource)

      createCharacterMessage <- getMessage(src, "create-character")
      _ <- writeMessage(createCharacterMessage)

      informName <- getMessage(src, "inform-character-name")
      informNameError <- getMessage(src, "error-invalid-name")
      name: Name <- ask(
        informName,
        informNameError,
        parseName
      )

      informGender <- getMessage(src, "inform-character-gender")
      informGenderError <- getMessage(src, "error-invalid-gender")
      gender: Gender <- ask(
        informGender,
        informGenderError,
        parseGender
      )

      informAge <- getMessage(src, "inform-character-age")
      informAgeError <- getMessage(src, "error-invalid-age")
      age: Age <- ask(
        informAge,
        informAgeError,
        parseAge
      )

      mc <- pure(MainCharacter(name, gender, age))

      characterCreated <- getMessage(src, "character-created", name.value)
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

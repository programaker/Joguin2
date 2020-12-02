package joguin.game.step.fight

import cats.free.Free
import cats.free.Free._
import cats.syntax.eq._
import cats.syntax.foldable._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import joguin.alien.invasion._
import joguin.earth.maincharacter.ExperienceR
import joguin.game.progress.Index
import joguin.game.progress._
import joguin.game.step.GameStep
import joguin.game.step.GameStep.Explore
import joguin.game.step.GameStep.GameOver
import joguin.game.step.fight.FightOption.FightAliens
import joguin.game.step.fight.FightOption.Retreat
import joguin.playerinteraction.interaction._
import joguin.playerinteraction.message.MessageSource.FightMessageSource
import joguin.playerinteraction.message.MessageSource.FightMessageSource._

import scala.concurrent.duration._

final class FightStep[F[_]](implicit env: FightStepEnv[F]) {
  import env._
  import interactionOps._
  import messageOps._
  import waitOps._

  def play(gameProgress: GameProgress, selectedInvasion: Index): Free[F, GameStep] =
    invasionByIndex(gameProgress, selectedInvasion)
      .map { invasion =>
        for {
          src <- getLocalizedMessageSource(FightMessageSource)

          updatedProgress <- if (isInvasionDefeated(gameProgress, selectedInvasion)) {
            cityAlreadySaved(gameProgress, invasion, src)
          } else {
            fightOrRetreat(gameProgress, invasion, selectedInvasion, src)
          }
        } yield Explore(updatedProgress): GameStep
      }
      .getOrElse(pure(GameOver))

  private def cityAlreadySaved(
    gameProgress: GameProgress,
    invasion: Invasion,
    src: LocalizedFightMessageSource
  ): Free[F, GameProgress] =
    getMessageFmt(src)(city_already_saved, List(CityNameField.get(invasion)))
      .flatMap(writeMessage)
      .map(_ => gameProgress)

  private def fightOrRetreat(
    gameProgress: GameProgress,
    invasion: Invasion,
    invasionIndex: Index,
    src: LocalizedFightMessageSource
  ): Free[F, GameProgress] = {
    val device = DefensePowerField.get(invasion).toString
    val character: String = CharacterNameField.get(gameProgress)
    val city: String = CityNameField.get(invasion)

    val option = for {
      message <- pure(getMessage(src)(_))

      report <- getMessageFmt(src)(report, List(character, city, device))
      _      <- writeMessage(report)

      giveOrder    <- message(give_order)
      errorMessage <- message(error_invalid_option)
      option       <- ask(giveOrder, errorMessage, parseFightOption)
    } yield option

    option.flatMap {
      case FightAliens => fight(gameProgress, invasion, invasionIndex, src)
      case Retreat     => pure(gameProgress)
    }
  }

  private def fight(
    gameProgress: GameProgress,
    invasion: Invasion,
    invasionIndex: Index,
    src: LocalizedFightMessageSource
  ): Free[F, GameProgress] = {
    val characterExperience: Int = ExperienceField.get(gameProgress)
    val deviceDefensePower: Int = DefensePowerField.get(invasion)
    val city: String = CityNameField.get(invasion)
    val deviceDestroyed = characterExperience >= deviceDefensePower

    showFightAnimation(src).flatMap { _ =>
      val up1 = refineV[ExperienceR](deviceDefensePower / 2)
        .map(increaseMainCharacterExperience(gameProgress, _))
        .getOrElse(gameProgress)

      val newExperience = ExperienceField.get(up1).toString

      val (up2, fightOutCome) =
        if (deviceDestroyed) {
          (defeatInvasion(up1, invasionIndex), getMessageFmt(src)(earth_won, List(newExperience)))
        } else {
          (up1, getMessageFmt(src)(aliens_won, List(city, newExperience)))
        }

      fightOutCome.flatMap(writeMessage).map(_ => up2)
    }
  }

  private def showFightAnimation(src: LocalizedFightMessageSource): Free[F, Unit] = {
    val time = 100.milliseconds

    for {
      message <- pure(getMessage(src)(_))

      earth       <- message(animation_earth)
      earthWeapon <- message(animation_earth_weapon)
      alien       <- message(animation_alien)
      alienWeapon <- message(animation_alien_weapon)
      strike      <- message(animation_strike)

      _ <- showAttack(earth, earthWeapon, strike)
      _ <- waitFor(time)

      _ <- showAttack(alien, alienWeapon, strike)
      _ <- waitFor(time)
    } yield ()
  }

  private def showAttack(attacker: String, weapon: String, strike: String): Free[F, Unit] =
    for {
      _ <- writeMessage("\n")
      _ <- writeMessage(attacker)

      _ <- showWeaponUse(weapon, 31)

      _ <- writeMessage(strike)
      _ <- writeMessage("\n")
    } yield ()

  private def showWeaponUse(weapon: String, count: Int): Free[F, Unit] =
    LazyList.range(1, count + 1).foldLeftM(()) { (_, i) =>
      for {
        _ <- if (i % 2 === 0) writeMessage(weapon) else writeMessage(" ")
        _ <- waitFor(50.milliseconds)
      } yield ()
    }
}

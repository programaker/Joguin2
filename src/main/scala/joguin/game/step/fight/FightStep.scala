package joguin.game.step.fight

import cats.free.Free
import cats.free.Free._
import cats.implicits._
import eu.timepit.refined._
import joguin.alien.Invasion
import joguin.earth.maincharacter.ExperienceR
import joguin.game.progress.Index
import joguin.game.progress._
import joguin.game.step.GameStep
import joguin.game.step.GameStep.Explore
import joguin.game.step.GameStep.GameOver
import joguin.game.step.fight.FightOption.FightAliens
import joguin.game.step.fight.FightOption.Retreat
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.interaction._
import joguin.playerinteraction.message.LocalizedMessageSource
import joguin.playerinteraction.message.MessageSource.FightMessageSource
import joguin.playerinteraction.message.MessagesOps
import joguin.playerinteraction.wait.WaitOps

import scala.concurrent.duration._

final class FightStep[F[_]](
  implicit
  m: MessagesOps[F],
  i: InteractionOps[F],
  w: WaitOps[F]
) {
  import FightMessageSource._
  import i._
  import m._
  import w._

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
    src: LocalizedMessageSource[FightMessageSource.type]
  ): Free[F, GameProgress] =
    getMessageFmt(src)(city_already_saved, List(invasion.city.name.value))
      .flatMap(writeMessage)
      .map(_ => gameProgress)

  private def fightOrRetreat(
    gameProgress: GameProgress,
    invasion: Invasion,
    invasionIndex: Index,
    src: LocalizedMessageSource[FightMessageSource.type]
  ): Free[F, GameProgress] = {

    val device = invasion.terraformDevice.defensePower.value.toString
    val character = gameProgress.mainCharacter.name.value
    val city = invasion.city.name.value

    val option = for {
      report <- getMessageFmt(src)(report, List(character, city, device))
      _      <- writeMessage(report)

      giveOrder    <- getMessage(src)(give_order)
      errorMessage <- getMessage(src)(error_invalid_option)
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
    src: LocalizedMessageSource[FightMessageSource.type]
  ): Free[F, GameProgress] = {

    val characterExperience = gameProgress.mainCharacter.experience.value //TODO => Lenses!
    val deviceDefensePower = invasion.terraformDevice.defensePower.value
    val city = invasion.city.name.value
    val deviceDestroyed = characterExperience >= deviceDefensePower

    showFightAnimation(src).flatMap { _ =>
      val up1 = refineV[ExperienceR](deviceDefensePower / 2)
        .map(increaseMainCharacterExperience(gameProgress, _))
        .getOrElse(gameProgress)

      val newExperience = up1.mainCharacter.experience.value.toString

      val (up2, fightOutCome) =
        if (deviceDestroyed) {
          (defeatInvasion(up1, invasionIndex), getMessageFmt(src)(earth_won, List(newExperience)))
        } else {
          (up1, getMessageFmt(src)(aliens_won, List(city, newExperience)))
        }

      fightOutCome.flatMap(writeMessage).map(_ => up2)
    }
  }

  private def showFightAnimation(src: LocalizedMessageSource[FightMessageSource.type]): Free[F, Unit] = {
    val time = 100.milliseconds

    for {
      earth       <- getMessage(src)(animation_earth)
      earthWeapon <- getMessage(src)(animation_earth_weapon)
      alien       <- getMessage(src)(animation_alien)
      alienWeapon <- getMessage(src)(animation_alien_weapon)
      strike      <- getMessage(src)(animation_strike)

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

      _ <- showWeaponUse(weapon, 1, 31)

      _ <- writeMessage(strike)
      _ <- writeMessage("\n")
    } yield ()

  private def showWeaponUse(weapon: String, start: Int, end: Int): Free[F, Unit] =
    if (start <= end) {
      val res = for {
        _ <- if (start % 2 === 0) {
          writeMessage(weapon)
        } else {
          writeMessage(" ")
        }

        _ <- waitFor(50.milliseconds)
      } yield ()

      res.flatMap(_ => showWeaponUse(weapon, start + 1, end))
    } else {
      pure(())
    }
}

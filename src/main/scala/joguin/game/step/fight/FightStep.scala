package joguin.game.step.fight

import cats.implicits._
import cats.free.Free
import cats.free.Free._
import eu.timepit.refined._
import eu.timepit.refined.numeric.NonNegative
import joguin.alien.Invasion
import joguin.game.progress.{GameProgress, Index}
import joguin.game.step.GameStepOps.NextGameStep
import joguin.game.step.{Explore, GameOver}
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.{FightMessageSource, LocalizedMessageSource, MessageSourceOps, MessagesOps}
import joguin.playerinteraction.wait.WaitOps

import scala.concurrent.duration._

final class FightStep(
  implicit s: MessageSourceOps[FightF],
  m: MessagesOps[FightF],
  i: InteractionOps[FightF],
  w: WaitOps[FightF]
) {
  import i._
  import m._
  import s._
  import w._

  def start(gameProgress: GameProgress, selectedInvasion: Index): Free[FightF, NextGameStep] =
    gameProgress
      .invasionByIndex(selectedInvasion)
      .map { invasion =>
        for {
          src <- getLocalizedMessageSource(FightMessageSource)

          updatedProgress <- if (gameProgress.isInvasionDefeated(selectedInvasion)) {
            cityAlreadySaved(gameProgress, invasion, src)
          } else {
            fightOrRetreat(gameProgress, invasion, selectedInvasion, src)
          }
        } yield Explore(updatedProgress): NextGameStep
      }
      .getOrElse(pure(GameOver))

  private def cityAlreadySaved(
    gameProgress: GameProgress,
    invasion: Invasion,
    src: LocalizedMessageSource
  ): Free[FightF, GameProgress] = {

    getMessageFmt(src, "city-already-saved", List(invasion.city.name.value))
      .flatMap(writeMessage)
      .map(_ => gameProgress)
  }

  private def fightOrRetreat(
    gameProgress: GameProgress,
    invasion: Invasion,
    invasionIndex: Index,
    src: LocalizedMessageSource
  ): Free[FightF, GameProgress] = {

    val device = invasion.terraformDevice.defensePower.value.toString
    val character = gameProgress.mainCharacter.name.value
    val city = invasion.city.name.value

    val option = for {
      report <- getMessageFmt(src, "report", List(character, city, device))
      _ <- writeMessage(report)

      giveOrder <- getMessage(src, "give-order")
      errorMessage <- getMessage(src, "error-invalid-option")
      option <- ask(giveOrder, errorMessage, FightOption.parse)
    } yield option

    option.flatMap {
      case FightAliens => fight(gameProgress, invasion, invasionIndex, src)
      case Retreat => pure(gameProgress)
    }
  }

  private def fight(
    gameProgress: GameProgress,
    invasion: Invasion,
    invasionIndex: Index,
    src: LocalizedMessageSource
  ): Free[FightF, GameProgress] = {

    val characterExperience = gameProgress.mainCharacterExperience.value
    val deviceDefensePower = invasion.terraformDevice.defensePower.value
    val city = invasion.city.name.value
    val deviceDestroyed = characterExperience >= deviceDefensePower

    showFightAnimation(src).flatMap { _ =>
      val up1 = refineV[NonNegative](deviceDefensePower / 2)
        .map(gameProgress.increaseMainCharacterExperience)
        .getOrElse(gameProgress)

      val newExperience = up1.mainCharacterExperience.value.toString

      val (up2, fightOutCome) =
        if (deviceDestroyed) {
          (up1.defeatInvasion(invasionIndex), getMessageFmt(src, "earth-won", List(newExperience)))
        } else {
          (up1, getMessageFmt(src, "aliens-won", List(city, newExperience)))
        }

      fightOutCome.flatMap(writeMessage).map(_ => up2)
    }
  }

  private def showFightAnimation(src: LocalizedMessageSource): Free[FightF, Unit] = {
    val time = 100.milliseconds

    for {
      earth <- getMessage(src, "animation-earth")
      earthWeapon <- getMessage(src, "animation-earth-weapon")
      alien <- getMessage(src, "animation-alien")
      alienWeapon <- getMessage(src, "animation-alien-weapon")
      strike <- getMessage(src, "animation-strike")

      _ <- showAttack(earth, earthWeapon, strike)
      _ <- waitFor(time)

      _ <- showAttack(alien, alienWeapon, strike)
      _ <- waitFor(time)
    } yield ()
  }

  private def showAttack(attacker: String, weapon: String, strike: String): Free[FightF, Unit] = {
    for {
      _ <- writeMessage("\n")
      _ <- writeMessage(attacker)

      _ <- showWeaponUse(weapon, 1, 31)

      _ <- writeMessage(strike)
      _ <- writeMessage("\n")
    } yield ()
  }

  private def showWeaponUse(weapon: String, start: Int, end: Int): Free[FightF, Unit] = {
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
}

sealed trait FightOption
case object FightAliens extends FightOption
case object Retreat extends FightOption

object FightOption {
  def parse(s: String): Option[FightOption] =
    refineV[FightAliensOrRetreat](s.toLowerCase).toOption
      .map(_.value)
      .map {
        case "f" => FightAliens
        case "r" => Retreat
      }
}
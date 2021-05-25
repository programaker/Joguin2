package joguin.game

import eu.timepit.refined.api.Refined
import joguin.refined.auto._
import eu.timepit.refined.numeric.NonNegative
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.refineV
import joguin.IdxSeq
import joguin.Name
import joguin.alien.invasion._
import joguin.alien.invasion.Invasion
import joguin.earth.maincharacter.Experience
import joguin.earth.maincharacter.ExperienceR
import monocle.Lens
import monocle.macros.GenLens
import monocle.syntax.all._
import monocle.AppliedOptional

package object progress {
  type CountR = NonNegative
  type Count = Int Refined CountR

  type IndexR = Positive
  type Index = Int Refined IndexR

  type InvasionsApplyOptional = AppliedOptional[IdxSeq[Invasion], Invasion]
  type DefeatedApplyOptional = AppliedOptional[IdxSeq[Invasion], Boolean]

  val ExperienceField: Lens[GameProgress, Experience] = GenLens[GameProgress](_.mainCharacter.experience)
  val CharacterNameField: Lens[GameProgress, Name] = GenLens[GameProgress](_.mainCharacter.name)
  val InvasionsField: Lens[GameProgress, IdxSeq[Invasion]] = GenLens[GameProgress](_.invasions)

  def InvasionFieldAtIndex(gp: GameProgress, index: Index): InvasionsApplyOptional =
    //1-based index, to match the invasion list as the player sees it
    //and also the player's input when select an invasion to fight
    gp.invasions.focus().index(index - 1)

  def DefeatedFieldAtIndex(gp: GameProgress, index: Index): DefeatedApplyOptional =
    InvasionFieldAtIndex(gp, index).composeLens(DefeatedField)

  def invasionByIndex(gp: GameProgress, index: Index): Option[Invasion] =
    InvasionFieldAtIndex(gp, index).getOption

  def isInvasionDefeated(gp: GameProgress, index: Index): Boolean =
    invasionByIndex(gp, index).exists(_.defeated)

  def increaseMainCharacterExperience(gp: GameProgress, experiencePoints: Experience): GameProgress =
    refineV[ExperienceR](ExperienceField.get(gp) + experiencePoints)
      .map(ExperienceField.set(_)(gp))
      .getOrElse(gp)

  def allInvasionsDefeated(gp: GameProgress): Boolean =
    gp.invasions.forall(_.defeated)

  def defeatInvasion(gp: GameProgress, index: Index): GameProgress =
    InvasionsField.set(DefeatedFieldAtIndex(gp, index).modify(_ => true))(gp)
}

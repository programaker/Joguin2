package joguin.game.step

import eu.timepit.refined.W
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.refineV
import eu.timepit.refined.string.MatchesRegex
import joguin.game.progress.Count
import joguin.game.progress.IndexR
import eu.timepit.refined.auto._

package object explore {
  type ExploreOptionR = MatchesRegex[W.`"""^[1-9][0-9]*$"""`.T] Or Equal[W.`"q"`.T]

  def parseExploreOption(s: String, invasionCount: Count): Option[ExploreOption] =
    refineV[ExploreOptionR](s.toLowerCase).toOption
      .map(_.value)
      .flatMap {
        case "q" =>
          Some(QuitGame)
        case index =>
          Some(index.toInt)
            .map(refineV[IndexR](_))
            .flatMap(_.toOption)
            .filter(_ <= invasionCount.value)
            .map(GoToInvasion)
      }
}

package joguin.game.step

import eu.timepit.refined.W
import eu.timepit.refined.auto._
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.refineV
import eu.timepit.refined.string.MatchesRegex
import joguin.game.progress.Count
import joguin.game.progress.IndexR
import joguin.game.step.explore.ExploreOption._

package object explore {
  type ExploreOptionR = MatchesRegex[W.`"""^[1-9][0-9]*$"""`.T] Or Equal[W.`"q"`.T]

  def parseExploreOption(s: String, invasionCount: Count): Option[ExploreOption] =
    refineV[ExploreOptionR](s.toLowerCase).toOption
      .flatMap(_.value match {
        case "q" =>
          Some(QuitGame)
        case index =>
          Some(index.toInt)
            .flatMap(refineV[IndexR](_).toOption)
            .filter(_ <= invasionCount)
            .map(GoToInvasion)
      })
}

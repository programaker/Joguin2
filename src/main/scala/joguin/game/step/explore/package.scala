package joguin.game.step

import eu.timepit.refined.auto._
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.refineV
import eu.timepit.refined.string.MatchesRegex
import joguin.game.progress.Count
import joguin.game.progress.IndexR
import joguin.game.step.explore.ExploreOption._
import joguin.playerinteraction.message.LocalizedMessageSource
import joguin.playerinteraction.message.MessageSource.ExploreMessageSource

package object explore {
  type LocalizedExploreMessageSource = LocalizedMessageSource[ExploreMessageSource.type]

  type ExploreOptionR = MatchesRegex["^[1-9][0-9]*$"] Or Equal["q"] Or Equal["Q"]

  def parseExploreOption(s: String, invasionCount: Count): Option[ExploreOption] =
    refineV[ExploreOptionR](s).toOption
      .flatMap(_.value match {
        case "q" | "Q" =>
          Some(QuitGame)
        case index =>
          Some(index.toInt)
            .flatMap(refineV[IndexR](_).toOption)
            .filter(_ <= invasionCount)
            .map(GoToInvasion)
      })
}

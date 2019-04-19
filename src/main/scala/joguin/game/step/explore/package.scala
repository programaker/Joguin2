package joguin.game.step

import cats.data.EitherK
import eu.timepit.refined.W
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.string.MatchesRegex
import joguin.playerinteraction.MessagesAndInteractionF
import joguin.playerinteraction.wait.WaitF

package object explore {
  type ExploreOptionR = MatchesRegex[W.`"""^[1-9][0-9]+$"""`.T] Or Equal[W.`"q"`.T]
  type ExploreF[A] = EitherK[MessagesAndInteractionF, WaitF, A]
}

package joguin.game.step

import cats.data.EitherK
import eu.timepit.refined.W
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.string.MatchesRegex
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.{MessageSourceF, MessagesF}
import joguin.playerinteraction.wait.WaitF

package object explore {
  type F1[A] = EitherK[MessageSourceF, MessagesF, A]
  type F2[A] = EitherK[F1, InteractionF, A]
  type ExploreF[A] = EitherK[F2, WaitF, A]

  type IndexOrQuit = MatchesRegex[W.`"""^[1-9][0-9]+$"""`.T] Or Equal[W.`"q"`.T]
}

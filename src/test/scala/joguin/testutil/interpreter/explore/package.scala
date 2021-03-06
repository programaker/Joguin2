package joguin.testutil.interpreter

import cats.data.EitherK
import cats.~>
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.MessagesF
import joguin.playerinteraction.wait.WaitF

package object explore {
  type F1[A] = EitherK[MessagesF, InteractionF, A]
  type ExploreStepF[A] = EitherK[WaitF, F1, A]

  /** ExploreStepF composite interpreter to State. To test the game step in isolation from the whole game */
  def exploreStepTestInterpreter(): ExploreStepF ~> MessageTrackState = {
    val i1 = MessagesInterpreter or InteractionInterpreter
    val iExplore = WaitInterpreter or i1

    iExplore
  }
}

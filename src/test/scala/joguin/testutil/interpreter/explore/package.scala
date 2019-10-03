package joguin.testutil.interpreter

import cats.data.EitherK
import cats.~>
import joguin.playerinteraction.interaction.InteractionF
import joguin.playerinteraction.message.MessageSourceF
import joguin.playerinteraction.message.MessagesF
import joguin.playerinteraction.wait.WaitF

package object explore {
  type F1[A] = EitherK[MessageSourceF, MessagesF, A]
  type F2[A] = EitherK[InteractionF, F1, A]
  type ExploreStepF[A] = EitherK[WaitF, F2, A]

  /** ExploreStepF composite interpreter to State. To test the game step in isolation from the whole game */
  def exploreStepTestInterpreter(): ExploreStepF ~> MessageTrackState = {
    val i1 = MessageSourceInterpreter or MessagesInterpreter
    val i2 = InteractionInterpreter or i1
    val iExplore = WaitInterpreter or i2

    iExplore
  }
}

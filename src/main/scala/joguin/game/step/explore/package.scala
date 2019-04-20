package joguin.game.step

import eu.timepit.refined.W
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.string.MatchesRegex

package object explore {
  type ExploreOptionR = MatchesRegex[W.`"""^[1-9][0-9]*$"""`.T] Or Equal[W.`"q"`.T]
}

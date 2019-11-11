package joguin.game.step.createcharacter

import joguin.alien.terraformdevice.PowerGeneratorOps
import joguin.earth.city.CityRepositoryOps
import joguin.playerinteraction.interaction.InteractionOps
import joguin.playerinteraction.message.MessagesOps

final class CreateCharacterStepEnv[F[_]](
  m: MessagesOps[F],
  i: InteractionOps[F],
  c: CityRepositoryOps[F],
  p: PowerGeneratorOps[F]
) {
  implicit val messageOps: MessagesOps[F] = m
  implicit val interactionOps: InteractionOps[F] = i
  implicit val cityRepositoryOps: CityRepositoryOps[F] = c
  implicit val powerGeneratorOps: PowerGeneratorOps[F] = p
}

object CreateCharacterStepEnv {
  implicit def createCharacterStepEnv[F[_]](
    implicit
    m: MessagesOps[F],
    i: InteractionOps[F],
    c: CityRepositoryOps[F],
    p: PowerGeneratorOps[F]
  ): CreateCharacterStepEnv[F] = new CreateCharacterStepEnv[F](m, i, c, p)
}

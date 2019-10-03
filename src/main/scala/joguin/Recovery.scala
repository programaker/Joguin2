package joguin

import cats.MonadError

object Recovery {
  def apply[F[_]](implicit F: MonadError[F, Throwable]): Recovery[F] = MonadError[F, Throwable]
}

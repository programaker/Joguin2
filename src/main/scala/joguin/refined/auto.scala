package joguin.refined

import eu.timepit.refined.api.Refined
import eu.timepit.refined.refineV
import eu.timepit.refined.api.Validate

object auto {
  implicit def typeToRefined[A, P](a: A)(implicit v: Validate[A, P]): Refined[A, P] =
    refineV[P].unsafeFrom(a)

  implicit def refinedToType[A, P](r: Refined[A, P]): A =
    r.value
}

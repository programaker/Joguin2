package joguin

import scalaz.Free

package object player {
  type Interact[A] = Free[InteractF,A]

  /*type ParseAnswerE[T] = ParseAnswerF[Either[String,T]]
  type ParseAnswer[T] = Free[ParseAnswerE, T]

  type ValidateAnswerE[T] = ValidateAnswerF[Either[String,T]]
  type ValidateAnswer[T] = Free[ValidateAnswerE, T]*/
}

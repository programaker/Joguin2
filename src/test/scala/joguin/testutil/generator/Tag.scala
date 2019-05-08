package joguin.testutil.generator

final case class Tag[T, A](value: A)

object Tag {
  trait T1
  trait T2
}

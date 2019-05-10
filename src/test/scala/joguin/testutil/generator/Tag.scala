package joguin.testutil.generator

final case class Tag[T, A](value: A)

object Tag {
  object implicits {
    implicit def unwrap[T, A](tag: Tag[T, A]): A = tag.value
  }

  trait T1
  trait T2
}

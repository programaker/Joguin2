package joguin.testutil.generator

final case class Tag[T, A](value: A) extends AnyVal {
  override def toString: String = s"$value"
}

object Tag {
  implicit def tagValue[T, A](tag: Tag[T, A]): A = tag.value
}

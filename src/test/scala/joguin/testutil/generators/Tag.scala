package joguin.testutil.generators

final case class Tag[T, A](value: A) extends AnyVal {
  @SuppressWarnings(Array("org.wartremover.warts.StringPlusAny"))
  override def toString: String = s"$value"
}

object Tag {
  implicit def tagValue[T, A](tag: Tag[T, A]): A = tag.value
}

package joguin.testutil.generator

import org.scalacheck.Arbitrary
import org.scalacheck.Gen

final case class Tag[T, A](value: A) extends AnyVal {
  override def toString: String = s"$value"
}

object Tag {
  object implicits {
    implicit def tagValue[T, A](tag: Tag[T, A]): A = tag.value
  }

  def arbTag[T, A](gen: Gen[A]): Arbitrary[Tag[T, A]] =
    Arbitrary(gen.map(Tag.apply))
}

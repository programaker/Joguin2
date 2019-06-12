package joguin.testutil

import org.scalatest.Matchers
import org.scalatest.PropSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

abstract class PropertyBasedSpec extends PropSpec with ScalaCheckPropertyChecks with Matchers

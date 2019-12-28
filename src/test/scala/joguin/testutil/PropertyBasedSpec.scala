package joguin.testutil

import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

abstract class PropertyBasedSpec extends AnyPropSpec with ScalaCheckDrivenPropertyChecks with Matchers

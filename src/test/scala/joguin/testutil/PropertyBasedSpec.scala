package joguin.testutil

import org.scalatest.{Matchers, PropSpec}
import org.scalatest.prop.PropertyChecks

abstract class PropertyBasedSpec extends PropSpec with PropertyChecks with Matchers

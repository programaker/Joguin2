package joguin.testutil

import org.scalatest.prop.PropertyChecks
import org.scalatest.Matchers
import org.scalatest.PropSpec

abstract class PropertyBasedSpec extends PropSpec with PropertyChecks with Matchers

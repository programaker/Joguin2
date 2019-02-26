package joguin

import eu.timepit.refined.api.Refined

package object earth {
  type Country = String Refined NonBlankString
}

package joguin

import eu.timepit.refined.api.Refined

package object earth {
  type CountryR = NonBlankString
  type Country = String Refined CountryR
}

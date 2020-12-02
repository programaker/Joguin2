val WartRemoverV = "2.4.13"
val SbtDependencyUpdatesV = "1.2.2"
val SbtScalaFmtV = "2.4.2"
val SbtAssemblyV = "0.15.0"

Seq(
  "org.wartremover" % "sbt-wartremover" % WartRemoverV,
  "org.jmotor.sbt" % "sbt-dependency-updates" % SbtDependencyUpdatesV,
  "org.scalameta" % "sbt-scalafmt" % SbtScalaFmtV,
  "com.eed3si9n" % "sbt-assembly" % SbtAssemblyV
).map(addSbtPlugin)

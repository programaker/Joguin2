name := "joguin2"

version := "2.0"
scalaVersion := "2.12.8"

scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.6.0",
  "org.typelevel" %% "cats-free" % "1.6.0",
  
  "eu.timepit" %% "refined" % "0.9.4",
  "eu.timepit" %% "refined-cats" % "0.9.4"
)

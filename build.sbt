name := "joguin2"
version := "2.0"

scalaVersion := "2.12.8"
scalacOptions += "-Ypartial-unification"

val catsOrg = "org.typelevel"
val catsVersion = "1.6.0"

val refinedOrg = "eu.timepit"
val refinedVersion = "0.9.4"

libraryDependencies ++= Seq(
  catsOrg %% "cats-core" % catsVersion,
  catsOrg %% "cats-free" % catsVersion,
  
  refinedOrg %% "refined" % refinedVersion,
  refinedOrg %% "refined-cats" % refinedVersion
)

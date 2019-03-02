name := "joguin2"
version := "2.0"

scalaVersion := "2.12.8"
scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-feature",
  "-explaintypes",
  
  "-Ypartial-unification",
  
  "-language:experimental.macros",
  "-language:postfixOps",
  "-language:existentials",
  "-language:higherKinds"
)

val catsOrg = "org.typelevel"
val catsVersion = "1.6.0"
val catsEffectVersion = "1.2.0"

val refinedOrg = "eu.timepit"
val refinedVersion = "0.9.4"

libraryDependencies ++= Seq(
  catsOrg %% "cats-core" % catsVersion,
  catsOrg %% "cats-free" % catsVersion,
  catsOrg %% "cats-effect" % catsEffectVersion,

  refinedOrg %% "refined" % refinedVersion,
  refinedOrg %% "refined-cats" % refinedVersion
)

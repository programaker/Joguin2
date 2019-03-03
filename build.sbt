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

val catsVersion = "1.6.0"
val catsEffectVersion = "1.2.0"
val refinedVersion = "0.9.4"
val commonsIoVersion = "2.6"
val circeVersion = "0.10.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-free" % catsVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion,

  "eu.timepit" %% "refined" % refinedVersion,
  "eu.timepit" %% "refined-cats" % refinedVersion,

  "commons-io" % "commons-io" % commonsIoVersion,

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion
)

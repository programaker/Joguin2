name := "joguin2"
version := "2.0"

scalaVersion := "2.12.8"
scalacOptions ++= Seq(
  "-encoding",
  "utf8",
  "-feature",
  "-explaintypes",
  "-Ypartial-unification",
  "-language:experimental.macros",
  "-language:postfixOps",
  "-language:existentials",
  "-language:higherKinds"
)

resolvers += Resolver.sonatypeRepo("releases")

val catsVersion = "1.6.0"
val catsEffectVersion = "1.2.0"
val refinedVersion = "0.9.4"
val commonsIoVersion = "2.6"
val circeVersion = "0.10.0"
val betterMonadicForVersion = "0.3.0-M4"
val kindProjectorVersion = "0.9.9"

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

wartremoverWarnings ++= Warts.allBut(
  Wart.Recursion,
  Wart.Nothing,
  Wart.ImplicitParameter,
  Wart.Any
)

addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForVersion)
addCompilerPlugin("org.spire-math" %% "kind-projector" % kindProjectorVersion)

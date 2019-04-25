name := "joguin2"
version := "2.0"
scalaVersion := "2.12.8"

val catsVersion = "1.6.0"
val catsEffectVersion = "1.2.0"
val refinedVersion = "0.9.4"
val commonsIoVersion = "2.6"
val circeVersion = "0.10.0"
val betterMonadicForVersion = "0.3.0-M4"
val kindProjectorVersion = "0.9.9"
val linterVersion = "0.1.17"
val scalatestVersion = "3.0.5"
val scalatestShapelessVersion = "1.2.0"

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForVersion)
addCompilerPlugin("org.spire-math" %% "kind-projector" % kindProjectorVersion)
addCompilerPlugin("org.psywerx.hairyfotr" %% "linter" % linterVersion)

scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-feature",
  "-explaintypes",
  "-deprecation",
  
  "-language:experimental.macros",
  "-language:postfixOps",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",

  "-Ywarn-dead-code",
  "-Ypartial-unification",
  "-Ywarn-value-discard", 
  "-Ywarn-unused:imports",
  "-Ywarn-unused:implicits",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates"
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-free" % catsVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion,

  "eu.timepit" %% "refined" % refinedVersion,
  "eu.timepit" %% "refined-cats" % refinedVersion,
  
  "commons-io" % "commons-io" % commonsIoVersion,
  
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,

  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % scalatestShapelessVersion % "test"
)

wartremoverWarnings ++= Warts.allBut(
  Wart.Recursion,
  Wart.Nothing,
  Wart.ImplicitParameter,
  Wart.Any
)

mainClass in assembly := Some("joguin.JoguinApplication")

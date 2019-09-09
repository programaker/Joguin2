name := "joguin2"
version := "2.0"
scalaVersion := "2.13.0"

val catsV = "2.0.0-RC3"
val refinedV = "0.9.9"
val commonsIoV = "2.6"
val circeV = "0.12.0-RC4"
val betterMonadicForV = "0.3.1"
val kindProjectorV = "0.10.3"
val linterV = "0.1.17"
val scalaTestV = "3.0.8"
val scalaCheckV = "1.14.0"
val scalaCheckShapelessV = "1.2.3"
val catsScalaCheckV = "0.1.1"
val zioV = "1.0.0-RC12-1"

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForV)
addCompilerPlugin("org.typelevel" %% "kind-projector" % kindProjectorV)

scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-feature",
  "-explaintypes",
  "-deprecation",
  
  "-language:experimental.macros",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",

  "-Ywarn-dead-code",
  "-Ywarn-value-discard", 
  "-Ywarn-unused:imports",
  "-Ywarn-unused:implicits",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates"
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % catsV,
  "org.typelevel" %% "cats-free" % catsV,

  "dev.zio" %% "zio" % zioV,

  "eu.timepit" %% "refined" % refinedV,

  "commons-io" % "commons-io" % commonsIoV,
  
  "io.circe" %% "circe-core" % circeV,
  "io.circe" %% "circe-generic" % circeV,
  "io.circe" %% "circe-parser" % circeV,

  "org.scalatest" %% "scalatest" % scalaTestV % "test",
  "org.scalacheck" %% "scalacheck" % scalaCheckV % "test",
  "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % scalaCheckShapelessV % "test"
)

wartremoverWarnings ++= Warts.allBut(
  Wart.Recursion,
  Wart.Nothing,
  Wart.ImplicitParameter,
  Wart.ImplicitConversion,
  Wart.Any,
  Wart.StringPlusAny
)

mainClass in assembly := Some("joguin.JoguinApplication")

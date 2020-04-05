name := "joguin2"
version := "2.0"
scalaVersion := "2.13.1"

val catsV = "2.1.1"
val catsEffectV = "2.1.2"
val refinedV = "0.9.13"
val commonsIoV = "2.6"
val circeV = "0.13.0"
val betterMonadicForV = "0.3.1"
val kindProjectorV = "0.10.3"
val scalaTestV = "3.1.1"
val scalatestPlusScalaCheckV = "3.1.1.1"
val scalaCheckShapelessV = "1.2.5"
val catsScalaCheckV = "0.2.0"
val monocleV = "2.0.4"

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

  "org.typelevel" %% "cats-effect" % catsEffectV,

  "eu.timepit" %% "refined" % refinedV,

  "commons-io" % "commons-io" % commonsIoV,
  
  "io.circe" %% "circe-core" % circeV,
  "io.circe" %% "circe-generic" % circeV,
  "io.circe" %% "circe-parser" % circeV,
	"io.circe" %% "circe-refined" % circeV,
	
	"com.github.julien-truffaut"  %%  "monocle-core" % monocleV,
	"com.github.julien-truffaut"  %%  "monocle-macro" % monocleV,

  "org.scalatest" %% "scalatest" % scalaTestV % "test",

  "org.scalatestplus" %% "scalacheck-1-14" % scalatestPlusScalaCheckV % "test",

  "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % scalaCheckShapelessV % "test",

  "io.chrisdavenport" %% "cats-scalacheck" % catsScalaCheckV % "test"
)

wartremoverWarnings ++= Warts.allBut(
  Wart.Recursion,
  Wart.Nothing,
  Wart.ImplicitParameter,
  Wart.Any,
  Wart.StringPlusAny
)

mainClass in assembly := Some("joguin.JoguinApplication")

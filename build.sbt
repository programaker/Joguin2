val ScalaV = "2.13.4"
val JoguinV = "2.0"

val CatsV = "2.3.0"
val CatsEffectV = "2.3.0"
val RefinedV = "0.9.18"
val CirceV = "0.13.0"
val BetterMonadicForV = "0.3.1"
val KindProjectorV = "0.10.3"
val ScalaTestV = "3.2.3"
val ScalatestPlusScalaCheckV = "3.1.1.1"
val ScalaCheckShapelessV = "1.2.5"
val CatsScalaCheckV = "0.3.0"
val MonocleV = "2.1.0"
val BetterFilesV = "3.9.1"

lazy val root = (project in file(".")).settings(
  organization := "com.github.programaker",
  name := "joguin2",
  version := JoguinV,
  scalaVersion := ScalaV,

  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % CatsV,
    "org.typelevel" %% "cats-free" % CatsV,

    "org.typelevel" %% "cats-effect" % CatsEffectV,

    "eu.timepit" %% "refined" % RefinedV,
    "eu.timepit" %% "refined-cats" % RefinedV,

    "io.circe" %% "circe-core" % CirceV,
    "io.circe" %% "circe-generic" % CirceV,
    "io.circe" %% "circe-parser" % CirceV,
    "io.circe" %% "circe-refined" % CirceV,

    "com.github.julien-truffaut" %% "monocle-core" % MonocleV,
    "com.github.julien-truffaut" %% "monocle-macro" % MonocleV,

    "com.github.pathikrit" %% "better-files" % BetterFilesV,

    "org.scalatest" %% "scalatest" % ScalaTestV % "test",
    "org.scalatestplus" %% "scalacheck-1-14" % ScalatestPlusScalaCheckV % "test",
    "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % ScalaCheckShapelessV % "test",
    "io.chrisdavenport" %% "cats-scalacheck" % CatsScalaCheckV % "test"
  ),

  Seq(
    "org.typelevel" %% "kind-projector" % KindProjectorV,
    "com.olegpy" %% "better-monadic-for" % BetterMonadicForV
  ).map(addCompilerPlugin)
)

ThisBuild / wartremoverErrors ++= Seq(
  Wart.FinalCaseClass,
  Wart.Throw,
  Wart.Return
)
ThisBuild / wartremoverWarnings ++= Warts.allBut(
  Wart.Recursion,
  Wart.ImplicitParameter,
  Wart.Any,
  Wart.Nothing,
  Wart.ImplicitConversion,
  Wart.Overloading,
  Wart.PlatformDefault
)

// disable Wartremover in console. Not only it's unnecessary but also cause error in Scala 2.13.2+
Compile / console / scalacOptions := (console / scalacOptions).value.filterNot(_.contains("wartremover"))

ThisBuild / scalacOptions ++= Seq(
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
  "-Ywarn-unused:explicits",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates",

  "-Ymacro-annotations"
)

mainClass in assembly := Some("joguin.JoguinApplication")

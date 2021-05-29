val JoguinV = "2.0"

val ScalaV = "3.0.0"

val CatsV = "2.6.1"
val CatsEffectV = "3.1.1"
val RefinedV = "0.9.25"
val CirceV = "0.14.1"
val BetterMonadicForV = "0.3.1"
val KindProjectorV = "0.13.0"
val ScalaTestV = "3.2.9"
val ScalatestPlusScalaCheckV = "3.2.9.0"
val ScalaCheckShapelessV = "1.3.0"
val CatsScalaCheckV = "0.3.0"
val MonocleV = "3.0.0-M6"
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

    "io.circe" %% "circe-core" % CirceV,
    "io.circe" %% "circe-generic" % CirceV,
    "io.circe" %% "circe-parser" % CirceV,
    "io.circe" %% "circe-refined" % CirceV,

    "com.github.julien-truffaut" %% "monocle-core" % MonocleV,
    "com.github.julien-truffaut" %% "monocle-macro" % MonocleV,

    ("com.github.pathikrit" %% "better-files" % BetterFilesV).cross(CrossVersion.for3Use2_13),

    "org.scalatest" %% "scalatest" % ScalaTestV % "test",
    "org.scalatestplus" %% "scalacheck-1-15" % ScalatestPlusScalaCheckV % "test",
    
    ("com.github.alexarchambault" %% "scalacheck-shapeless_1.15" % ScalaCheckShapelessV % "test")
      .cross(CrossVersion.for3Use2_13),
    
    ("io.chrisdavenport" %% "cats-scalacheck" % CatsScalaCheckV % "test").cross(CrossVersion.for3Use2_13)
  )
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
  "-source:3.0-migration",
  "-encoding", "utf8",
  "-deprecation",
  "-explain",
  "-language:implicitConversions"
)

assembly / mainClass := Some("joguin.JoguinApplication")

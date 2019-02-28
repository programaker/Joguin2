name := "joguin2"
version := "2.0"

scalaVersion := "2.12.8"
scalacOptions ++= Seq(
  "-encoding", "utf8",
  "-feature",
  "-explaintypes",
  
  "-P:wartremover:only-warn-traverser:org.wartremover.warts.Unsafe",
  "-Ypartial-unification",
  
  "-language:experimental.macros",
  "-language:postfixOps",
  "-language:existentials",
  "-language:higherKinds"
)

resolvers += Resolver.sonatypeRepo("releases")

val catsOrg = "org.typelevel"
val catsVersion = "1.6.0"

val refinedOrg = "eu.timepit"
val refinedVersion = "0.9.4"

val wartremoverOrg = "org.wartremover"
val wartremoverVersion = "2.4.1"

libraryDependencies ++= Seq(
  catsOrg %% "cats-core" % catsVersion,
  catsOrg %% "cats-free" % catsVersion,

  refinedOrg %% "refined" % refinedVersion,
  refinedOrg %% "refined-cats" % refinedVersion
)

addCompilerPlugin(wartremoverOrg %% "wartremover" % wartremoverVersion)
name := "play2letsencrypt"

// If the CI supplies a "build.version" environment variable, inject it as the rev part of the version number:
version := s"${sys.props.getOrElse("build.majorMinor", "0.1")}.${sys.props.getOrElse("build.version", "SNAPSHOT")}"

scalaVersion := "2.11.7"

organization := "com.themillhousegroup"

val targetPlayVersion = "2.5.12"


libraryDependencies ++= Seq(
  "com.typesafe.play"           %%  "play"                  % targetPlayVersion       % "provided",
  "com.themillhousegroup"       %%  "mondrian"              % "0.4.60",
  "com.themillhousegroup"       %% "sses"                   % "1.0.10",
  "com.typesafe.play"           %%  "play-specs2"           % targetPlayVersion       % "test",
  "org.mockito"                 %   "mockito-all"           % "1.10.19"               % "test"
)

resolvers ++= Seq(  "oss-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
                    "oss-releases"  at "https://oss.sonatype.org/content/repositories/releases",
                    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/")

jacoco.settings

publishArtifact in (Compile, packageDoc) := false

seq(bintraySettings:_*)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalariformSettings

net.virtualvoid.sbt.graph.Plugin.graphSettings


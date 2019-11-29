import com.softwaremill.PublishTravis.publishTravisSettings
import sbt.Keys.unmanagedSourceDirectories

val v2_12 = "2.12.10"
val v2_13 = "2.13.1"

lazy val commonSettings = commonSmlBuildSettings ++ ossPublishSettings ++ acyclicSettings ++ Seq(
  organization := "com.softwaremill.safepath",
  scalaVersion := v2_12,
  scalafmtOnCompile := true,
  crossScalaVersions := Seq(v2_12, v2_13),
  libraryDependencies ++= Seq(compilerPlugin("com.softwaremill.neme" %% "neme-plugin" % "0.0.5")),
  scmInfo := Some(ScmInfo(url("https://github.com/softwaremill/safepath"), "git@github.com:softwaremill/safepath.git"))
)

lazy val core: Project = (project in file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := "safepath-core",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "org.scalatest" %% "scalatest" % "3.0.8" % "test"
    ),
    unmanagedSourceDirectories in Compile += {
      val sourceDir = (baseDirectory in Compile).value / "src" / "main"
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, n)) if n >= 13 => sourceDir / "scala-2.13+"
        case _                       => sourceDir / "scala-2.13-"
      }
    }
  )

lazy val rootProject = (project in file("."))
  .settings(commonSettings: _*)
  .settings(publishArtifact := false, name := "safepath")
  .settings(publishTravisSettings)
  .aggregate(
    core
  )

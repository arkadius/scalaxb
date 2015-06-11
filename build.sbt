import Dependencies._
import Common._

lazy val commonSettings = Seq(
    version in ThisBuild := "1.3.0-SNAPSHOT",
    organization in ThisBuild := "org.scalaxb",
    homepage in ThisBuild := Some(url("http://scalaxb.org")),
    licenses in ThisBuild := Seq("MIT License" -> url("https://github.com/eed3si9n/scalaxb/blob/master/LICENSE")),
    description in ThisBuild := """scalaxb is an XML data-binding tool for Scala that supports W3C XML Schema (xsd) and wsdl.""",
    scalacOptions := Seq("-deprecation", "-unchecked"),
    parallelExecution in Test := false,
    resolvers += Resolver.typesafeIvyRepo("releases")
  ) ++ sonatypeSettings ++ lsSettings

lazy val root = (project in file(".")).
  aggregate(app_2_10, app_2_11, integration, scalaxbPlugin).
  settings(
    scalaVersion := scala211,
    publishArtifact := false
  )

lazy val app = (project in file("cli")).
  settings(commonSettings: _*).
  settings(codegenSettings: _*).
  settings(
    name := "scalaxb",
    resolvers <+= sbtResolver,
    libraryDependencies ++= appDependencies(scalaVersion.value)
  ).
  cross

lazy val app_2_10 = app(scala210)

lazy val app_2_11 = app(scala211)

lazy val integration = (project in file("integration")).
  settings(commonSettings: _*).
  settings(
    crossScalaVersions := Seq(scala211),
    scalaVersion := scala211,
    publishArtifact := false,
    libraryDependencies ++= integrationDependencies(scalaVersion.value)
    // fork in test := true,
    // javaOptions in test ++= Seq("-Xmx2G", "-XX:MaxPermSize=512M")
  ).
  dependsOn(app_2_11)

lazy val scalaxbPlugin = (project in file("sbt-scalaxb")).
  settings(commonSettings: _*).
  settings(
    crossScalaVersions := Seq(scala210),
    scalaVersion := scala210,
    sbtPlugin := true,
    name := "sbt-scalaxb",
    // sbtVersion in Global := "0.12.4",
    // scalaVersion in Global := "2.9.2",
    description := """sbt plugin to run scalaxb"""
  ).
  dependsOn(app_2_10)

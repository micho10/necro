ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val nekotech      = "com.nekotech"
lazy val scala3Version = "3.4.2"

// Dependencies version
lazy val catsEffectVersion = "3.5.3"
lazy val circeVersion      = "0.14.7"
lazy val fs2CirceVersion   = "1.11.0"
lazy val http4sVersion     = "0.23.27"
lazy val pureConfigVersion = "0.17.6"
lazy val scalaMeta         = "1.0.0"

lazy val server = (project in file("."))
  .settings(
    name         := "Nekro",
    scalaVersion := scala3Version,
    organization := nekotech,
    libraryDependencies ++= Seq(
      /**
       * Cats Effect is a high-performance, asynchronous, composable framework for building real-world applications
       * in a purely functional style within the Typelevel ecosystem.
       */
      "org.typelevel"         %% "cats-effect"         % catsEffectVersion,
      /**
       * This module provides Builder and Tokenizer instances for the circe JSON type and a Tokenizer instance
       * for each type T having an implicit Encoder[T] in scope.
       */
      "org.gnieh"             %% "fs2-data-json-circe" % fs2CirceVersion,
//      "co.fs2" % "fs2-core_3" % "3.10.2",
      /**
       * JSON library for Scala
       */
      "io.circe"              %% "circe-generic"       % circeVersion,
      /**
       * Provides Circe codecs for http4s
       */
      "org.http4s"            %% "http4s-circe"        % http4sVersion,
      /**
       * Simple DSL for writing http4s services
       */
      "org.http4s"            %% "http4s-dsl"          % http4sVersion,
      /**
       * Ember implementation for http4s servers
       */
      "org.http4s"            %% "http4s-ember-server" % http4sVersion,
      /**
       * Scala testing library with actionable errors and extensible APIs
       */
      "org.scalameta"         %% "munit"               % scalaMeta          % Test,
      /**
       * PureConfig is a Scala library for loading configuration files. It reads Typesafe Config configurations
       * written in HOCON, Java Properties, or JSON to native Scala classes in a boilerplate-free way
       */
      "com.github.pureconfig" %% "pureconfig-core"     % pureConfigVersion
    )
  )

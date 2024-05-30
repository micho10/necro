package com.nekotech.nekro

import cats.effect.{IO, IOApp}
import cats.implicits.*
import com.nekotech.nekro.config.EmberConfig
import com.nekotech.nekro.config.Syntax.loadF
import com.nekotech.nekro.http.HttpApi
import org.http4s.ember.server.EmberServerBuilder
import pureconfig.ConfigSource

object Application extends IOApp.Simple:

  // 'default' means default config file in the resources directory (that's application.conf)
  // pureconfig will return errors on the left and config on the right
//  val configSource = ConfigSource.default.load[EmberConfig]

  // Ember is part of Http4s
  // HTTP API decorated with a function in case it doesn't match the supported endpoints
  // Build a resource
  override def run: IO[Unit] =
    ConfigSource.default.loadF[IO, EmberConfig].flatMap { config =>
      EmberServerBuilder
        .default[IO]
        .withHost(config.host)
        .withPort(config.port)
        .withHttpApp(HttpApi[IO].endpoints.orNotFound)
        .build
        .use(_ => IO.println("Let's play Necromunda!!") *> IO.never)
    }




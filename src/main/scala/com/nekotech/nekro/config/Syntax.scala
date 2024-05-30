package com.nekotech.nekro.config

import cats.MonadThrow
import cats.implicits.*
import pureconfig.error.ConfigReaderException
import pureconfig.{ConfigReader, ConfigSource}

import scala.reflect.ClassTag

object Syntax:

  /**
   * Extension method with the following parameters:
   *
   * F[_]: A higher-kinded type representing a generic container.
   * A: The type of the configuration object to be read.
   * It also requires the following implicit parameters:
   *
   *    - reader: ConfigReader[A]: An implicit ConfigReader for type A. This will be used to read the configuration.
   *    - F: MonadThrow[F]:        An implicit MonadThrow instance for the type constructor F. This provides the
   *                               ability to handle errors within the monad.
   *    - tag: ClassTag[A]:        An implicit ClassTag for type A. This is used for runtime reflection.
   */
  extension (source: ConfigSource)
    def loadF[F[_], A](using reader: ConfigReader[A], F: MonadThrow[F], tag: ClassTag[A]): F[A] =
      // F[Either[Errors, A]]
      F.pure(source.load[A]).flatMap {
        case Left(errors) => F.raiseError[A](ConfigReaderException(errors))
        case Right(value) => F.pure(value)
      }


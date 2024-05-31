package com.nekotech.nekro.config

import com.comcast.ip4s.{Host, Port}
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert
import pureconfig.generic.derivation.default.*

/**
 * In order to parse the configuration parameters automatically from application.conf, we need a given (implicit)
 * configReader
 *
 * given configReader: ConfigReader[EmberConfig]
 *                     -------------------------
 *                     This can be derived automatically from the config library
 *
 * Derives => The library pureconfig can automatically generate a ConfigReader of the particular type we need
 *
 * This emberConfig must be obtained and passed to the EmberServerBuilder
 *
 * @param host Server host IP address
 * @param port Server port
 */
final case class EmberConfig (host: Host, port: Port) derives ConfigReader

// Changing the types from String the ConfigReader cannot be automatically derived.
// It's necessary to write a custom ConfigReader

object EmberConfig:
  // Need given ConfigReader[Host] + ConfigReader[Port] => compiler generates ConfigReader[EmberConfig]

  given hostReader: ConfigReader[Host] = ConfigReader[String].emap { hostString =>
    Host
      .fromString(hostString)
      .toRight(
        CannotConvert(hostString, Host.getClass.toString, s"Invalid host string: $hostString")
      )
  }

  given portReader: ConfigReader[Port] = ConfigReader[Int].emap { portInt =>
    Port
      .fromInt(portInt)
      .toRight(
        CannotConvert(portInt.toString, Port.getClass.toString, s"Invalid port number: $portInt")
      )
  }

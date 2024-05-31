package com.nekotech.nekro.http.routes

import cats.Monad
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

/**
 * Store the health endpoints
 *
 * Constructor is private
 */
class HealthRoutes[F[_]: Monad] private extends Http4sDsl[F]:

  private val healthRoute: HttpRoutes[F] = HttpRoutes.of[F] {
      case GET -> Root => Ok("All going great!")
      }

  /**
   * Path prefixes for all the endpoints this class is responsible for.
   *
   * This is how endpoints are exposed.
   */
  val routes: HttpRoutes[F] = Router (
    "/health" -> healthRoute
  )



/**
 * Companion object
 */
object HealthRoutes:
  /**
   * Factory method
   *
   * @tparam F
   * @return
   */
  def apply[F[_]: Monad] = new HealthRoutes[F]


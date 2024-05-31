package com.nekotech.nekro.http

import cats.*
import cats.effect.Concurrent
import cats.implicits.*
import com.nekotech.nekro.http.routes.{HealthRoutes, ScenarioRoutes}
import org.http4s.HttpRoutes
import org.http4s.server.Router

/**
 * Unify all the set up endpoints.
 */
class HttpApi[F[_]: Concurrent]: 
  private val healthRoutes = HealthRoutes[F].routes
  private val scenarioRoutes = ScenarioRoutes[F].routes

  val endpoints: HttpRoutes[F] = Router(
    // Prefix for all endpoints
    "/api" -> (healthRoutes <+> scenarioRoutes)
  )



/** Companion object */
object HttpApi: 
  def apply[F[_] : Concurrent] = new HttpApi[F]


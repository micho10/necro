package com.nekotech.nekro.http

import cats.*
import cats.effect.Concurrent
import cats.implicits.*
import com.nekotech.nekro.http.routes.{HealthRoutes, JobRoutes}
import org.http4s.server.Router

/**
 * Unify all the set up endpoints.
 */
class HttpApi[F[_]: Concurrent] {
  private val healthRoutes = HealthRoutes[F].routes
  private val jobRoutes = JobRoutes[F].routes

  val endpoints = Router(
    // Prefix for all endpoints
    "/api" -> (healthRoutes <+> jobRoutes)
  )
}


/** Companion object */
object HttpApi {
  def apply[F[_] : Concurrent] = new HttpApi[F]
//  def apply[F[_]: Monad](): HttpApi[F] = new HttpApi()
}

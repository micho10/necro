package com.nekotech.rikusho.http

import cats.*
import cats.implicits.*
import com.nekotech.rikusho.http.routes.{HealthRoutes, JobRoutes}
import org.http4s.server.Router

/**
 * Unify all the set up endpoints.
 */
class HttpApi[F[_]: Monad] {
  private val healthRoutes = HealthRoutes[F].routes
  private val jobRoutes = JobRoutes[F].routes

  val endpoints = Router(
    // Prefix for all endpoints
    "/api" -> (healthRoutes <+> jobRoutes)
  )
}


// Companion object
object HttpApi {
  def apply[F[_] : Monad] = new HttpApi[F]
//  def apply[F[_]: Monad](): HttpApi[F] = new HttpApi()
}

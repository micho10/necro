package com.nekotech.rikusho.http.routes

import cats.*
import cats.implicits.*
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

import scala.language.postfixOps

/**
 * Store the CRUD endpoints
 *
 * Constructor is private
 */
class JobRoutes[F[_]: Monad] private extends Http4sDsl[F]:
  /**
   * Http4s DSL defined properties:
   *    - Root
   *    - UUIDVar
   */

  /**
   * Added parameters for pagination
   * It's POST to be able to pass some filters as payload
   */
  //POST /jobs?offset=x&limit=y { filters } // TODO add query params & filters
  private val allJobsRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case POST -> Root =>
      Ok("TODO")
  }

  //GET /jobs/uuid
  private val findJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / UUIDVar(id) =>
      Ok(s"TODO find job for $id")
  }

  //POST /jobs/create { jobInfo }
  private val createJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case POST -> Root / "create" =>
      Ok("TODO")
  }

  //PUT /jobs/uuid { jobInfo }
  private val updateJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case PUT -> Root / UUIDVar(id) =>
      Ok(s"TODO update job at $id")
  }

  //DELETE /jobs/uuid
  private val deleteJobRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case DELETE -> Root / UUIDVar(id) =>
      Ok(s"TODO delete job at $id")
  }


  /**
   * Path prefixes for all the endpoints this class is responsible for.
   *
   * This is how endpoints are exposed.
   */
  val routes = Router(
    // <+> Cats' semigroup K combinator
    "/jobs" -> (allJobsRoute <+> findJobRoute <+> createJobRoute <+> updateJobRoute <+> deleteJobRoute)
  )



/**
 * Companion object
 */
object JobRoutes:
  /**
   * Factory method
   *
   * @tparam F
   * @return
   */
  def apply[F[_]: Monad] = new JobRoutes[F]


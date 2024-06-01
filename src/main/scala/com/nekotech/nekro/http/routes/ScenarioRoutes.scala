package com.nekotech.nekro.http.routes

// It must be imported 1st to avoid issues with implicits
//import io.circe.generic.auto.*
import fs2.data.json.*
import fs2.data.json.circe.*

import cats.*
import cats.effect.*
import cats.implicits.*
import com.nekotech.nekro.domain.Scenario.{Scenario, ScenarioInfo}
import com.nekotech.nekro.http.responses.FailureResponse
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

import java.util.UUID
import scala.collection.mutable
import scala.language.postfixOps

/**
 * Store the CRUD endpoints
 *
 * Constructor is private
 */
class ScenarioRoutes[F[_]: Concurrent] private extends Http4sDsl[F]:

  // Database
  private val database = mutable.Map[UUID, Scenario]()

  /**
   * Http4s DSL defined properties:
   *    - Root
   *    - UUIDVar
   */

  /**
   * Added parameters for pagination
   * It's POST to be able to pass some filters as payload
   */
  //POST /scenarios?offset=x&limit=y { filters } // TODO add query params & filters
  private val allScenariosRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case POST -> Root =>
      // database.values is an Iterable[Scenario], which needs to be serialized as JSON
      Ok(database.values)
  }

  //GET /scenarios/uuid
  private val findScenarioRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    // UUIDVar is built into Http4s as a DSL
    case GET -> Root / UUIDVar(id) =>
      database.get(id) match
        case Some(scenario) => Ok(scenario)
        case None           => NotFound(FailureResponse(s"Scenario $id not found"))
  }

  //POST /scenarios/create { scenarioInfo }
  private def createScenario(scenarioInfo: ScenarioInfo): F[Scenario] =
    Scenario(
      id = UUID.randomUUID(),
      fluff = Option("Fluff"),
      subtitle = Option("Subtitle"),
      scenarioInfo = scenarioInfo,
      active = true
    ).pure[F] // Applicative's (Monad) extension method wrapping any value in an F

  private val createScenarioRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ POST -> Root / "create" =>
      // 1. Parse the payload out of scenarioInfo using circe
      // 2. Build a new scenario
      // 3. Store the scenario in the DB returning the final result
      for {
        scenarioInfo <- req.as[ScenarioInfo]
        scenario <- createScenario(scenarioInfo)
        resp <- Created(scenario.id)
      } yield resp
  }

  //PUT /scenarios/uuid { scenarioInfo }
  private val updateScenarioRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ PUT -> Root / UUIDVar(id) =>
      database.get(id) match
        case Some(scenario) =>
          for {
            scenarioInfo <- req.as[ScenarioInfo]
            _ <- database.put(id, scenario.copy(scenarioInfo = scenarioInfo)).pure[F]
            resp <- Ok()
          } yield resp
        case None => NotFound(FailureResponse(s"Cannot update scenario $id: not found"))
  }

  //DELETE /scenarios/uuid
  private val deleteScenarioRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ DELETE -> Root / UUIDVar(id) =>
      database.get(id) match
        case Some(scenario) =>
          for {
            _ <- database.remove(id).pure[F]
            resp <- Ok()
          } yield resp
        case None => NotFound(FailureResponse(s"Cannot delete scenario $id: not found"))
  }


  /**
   * Path prefixes for all the endpoints this class is responsible for.
   *
   * This is how endpoints are exposed.
   */
  val routes: HttpRoutes[F] = Router(
    // <+> Cats' semigroup K combinator
    "/scenarios" -> (allScenariosRoute <+> findScenarioRoute <+> createScenarioRoute <+> updateScenarioRoute <+> deleteScenarioRoute)
  )



/**
 * Companion object
 */
object ScenarioRoutes:
  /**
   * Factory method
   *
   * @tparam F
   * @return
   */
  def apply[F[_]: Concurrent] = new ScenarioRoutes[F]


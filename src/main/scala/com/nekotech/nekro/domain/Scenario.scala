package com.nekotech.nekro.domain

import java.util.UUID

object Scenario {

  case class Scenario(
                  id: UUID,
                  fluff: Option[String],
                  subtitle: Option[String],
                  scenarioInfo: ScenarioInfo,
                  active: Boolean = false
                )

  case class ScenarioInfo(
                      battleType: String,
                      battlefield: String,
                      crews: String,
                      deployment: String,
                      gangTactics: String,
                      endingTheBattle: String,
                      victory: String,
                      rewards: List[String], // Credits | experience | reputation
                      specialRules: Option[String],
                      reinforcements: Option[String],
                      fleeingTheBattlefield: Option[String],
                      attackerDefender: Option[String],
                      other: Option[String]
                    )

  object ScenarioInfo:
    val empty: ScenarioInfo =
      ScenarioInfo("", "", "", "", "", "", "", List.empty, None, None, None, None, None)

}

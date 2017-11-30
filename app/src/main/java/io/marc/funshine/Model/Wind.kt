package io.marc.funshine.Model

import org.json.JSONObject

/**
 *  Project Funshine
 *  Created by Marc on 30/11/2017.
 */
class Wind(wind: JSONObject) {
    val speed = wind.getDouble("speed")
}
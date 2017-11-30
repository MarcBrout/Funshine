package io.marc.funshine.Model

import org.json.JSONObject

/**
 *  Project Funshine
 *  Created by Marc on 30/11/2017.
 */
class Sky(weather: JSONObject) {
    val clouds = weather.getString("main")
    val description = weather.getString("description")
    val icon = weather.getString("icon")
}
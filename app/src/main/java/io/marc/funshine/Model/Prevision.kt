package io.marc.funshine.Model

import org.json.JSONObject

/**
 *  Project Funshine
 *  Created by Marc on 30/11/2017.
 */
class Prevision(jsonObject: JSONObject) {
    val date = jsonObject.getLong("dt")
    val dateString = jsonObject.getString("dt_txt")
    val temperature = Temperature(jsonObject.getJSONObject("main"))
    val sky = Sky(jsonObject.getJSONArray("weather").getJSONObject(0))
    val wind = Wind(jsonObject.getJSONObject("wind"))
}
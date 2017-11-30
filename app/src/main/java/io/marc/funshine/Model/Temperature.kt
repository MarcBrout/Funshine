package io.marc.funshine.Model

import org.json.JSONObject

/**
 *  Project Funshine
 *  Created by Marc on 30/11/2017.
 */
class Temperature(main: JSONObject) {
    val average = main.getDouble("temp").toInt()
    val min = "${main.getDouble("temp_min").toInt()}°"
    val max = "${main.getDouble("temp_max").toInt()}°"
    val pressure = main.getDouble("pressure")
    val sea_level = main.getDouble("sea_level")
    val humidity = main.getDouble("humidity")
}
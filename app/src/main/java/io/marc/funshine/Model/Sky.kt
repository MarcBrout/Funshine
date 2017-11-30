package io.marc.funshine.Model

import org.json.JSONObject

/**
 *  Project Funshine
 *  Created by Marc on 30/11/2017.
 */

class Sky(weather: JSONObject) {
    companion object {
        val miniIconMap = hashMapOf(
                Pair("01d", "sunny_mini"),
                Pair("01n", "sunny_mini"),
                Pair("02d", "partially_cloudy_mini"),
                Pair("02n", "partially_cloudy_mini"),
                Pair("03d", "cloudy_mini"),
                Pair("03n", "cloudy_mini"),
                Pair("04d", "cloudy_mini"),
                Pair("04n", "cloudy_mini"),
                Pair("09d", "rainy_mini"),
                Pair("09n", "rainy_mini"),
                Pair("10d", "rainy_mini"),
                Pair("10n", "rainy_mini"),
                Pair("11d", "thunder_lightning_mini"),
                Pair("11n", "thunder_lightning_mini"),
                Pair("13d", "snow_mini"),
                Pair("13n", "snow_mini"),
                Pair("50d", "cloudy_mini"),
                Pair("50n", "cloudy_mini")
        )

        val iconMap = hashMapOf(
                Pair("01d", "sunny"),
                Pair("01n", "sunny"),
                Pair("02d", "partially_cloudy"),
                Pair("02n", "partially_cloudy"),
                Pair("03d", "cloudy"),
                Pair("03n", "cloudy"),
                Pair("04d", "cloudy"),
                Pair("04n", "cloudy"),
                Pair("09d", "rainy"),
                Pair("09n", "rainy"),
                Pair("10d", "rainy"),
                Pair("10n", "rainy"),
                Pair("11d", "thunder_lightning"),
                Pair("11n", "thunder_lightning"),
                Pair("13d", "snow"),
                Pair("13n", "snow"),
                Pair("50d", "cloudy"),
                Pair("50n", "cloudi")
        )
    }

    private val DRAWABLE = "drawable/"

    val clouds = weather.getString("main")
    val description = weather.getString("description")
    val miniIcon = "$DRAWABLE${miniIconMap[weather.getString("icon")]}"
    val icon = "$DRAWABLE${iconMap[weather.getString("icon")]}"
}
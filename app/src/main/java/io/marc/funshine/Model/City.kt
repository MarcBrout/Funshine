package io.marc.funshine.Model

import org.json.JSONObject

/**
 *  Project Funshine
 *  Created by Marc on 30/11/2017.
 */
class City(val name: String, val country: String) {
    val full_desc = "$name, $country"

    constructor(jsonObject: JSONObject) :
            this(jsonObject.getString("name"), jsonObject.getString("country"))

    override fun toString(): String {
        return "$name, $country"
    }
}
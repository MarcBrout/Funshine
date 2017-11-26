package io.marc.funshine

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class WeatherActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private val REQUEST_LOCATION_CODE = 0

    companion object {
        val API_KEY = "cbbf72c957ceaf9547eb3525a04c1066"
    }

    class Query private constructor(){
        enum class TemperatureUnits {
            CELSIUS,
            FAHRENHEIT,
            KELVIN
        }

        private lateinit var url: String

        private constructor(lat: Double, long: Double, unit: TemperatureUnits) : this() {
            url = "http://api.openweathermap.org/data/2.5/forecast?lat=$lat&lon=$long"
            url += when (unit) {
                TemperatureUnits.CELSIUS -> {"&units=metric"}
                TemperatureUnits.FAHRENHEIT -> {"&units=imperial"}
                TemperatureUnits.KELVIN -> {""}
            }
            url += "&APPID=$API_KEY"
        }

        fun getUrl() = url

        class Builder {
            private var lat: Double? = null
            private var long: Double? = null
            private var mode = TemperatureUnits.KELVIN


            fun setLat(lat: Double): Query.Builder {
                this.lat = lat
                return this
            }

            fun setLong(long: Double): Query.Builder {
                this.long = long
                return this
            }

            fun setTemperatureUnits(unit: TemperatureUnits) : Query.Builder {
                mode = unit
                return this
            }

            fun build() : Query {
                return Query(lat!!, long!!, mode)
            }
        }
    }

    fun downloadWeatherData() {
        val query = Query.Builder()
                .setLat(9.968782)
                .setLong(76.299076)
                .setTemperatureUnits(Query.TemperatureUnits.CELSIUS)
                .build()

        Log.d("FUNSHINE", query.getUrl())

        val jsonObjReq = JsonObjectRequest(Request.Method.GET, query.getUrl(), null,
                Response.Listener<JSONObject> { response ->
                    Log.d("FUNSHINE", "Res: $response")
                },
                Response.ErrorListener { error ->
                    Log.d("FUNSHINE", "Err: ${error.localizedMessage}")
                })
        //Volley.newRequestQueue(this).add(jsonObjReq)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_LOCATION_CODE -> {
                if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadWeatherData()
                } else {
                    Log.v("FUNSHINE", "User refused access to map location")
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION_CODE)
        } else {
            downloadWeatherData()
        }


    }


}

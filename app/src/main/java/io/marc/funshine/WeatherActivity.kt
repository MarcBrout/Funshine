package io.marc.funshine

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.marc.funshine.Adapters.WeatherAdapter
import io.marc.funshine.Model.City
import io.marc.funshine.Model.Prevision
import kotlinx.android.synthetic.main.activity_weather.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class WeatherActivity : AppCompatActivity(),
        ActivityCompat.OnRequestPermissionsResultCallback
{
    private val NORMAL_INTERVAL: Long = 600000
    private val DEBUG_INTERVAL: Long = 1000
    private val REQUEST_LOCATION_CODE = 0
    private lateinit var locationCallback: LocationCallback
    private val previsions = arrayListOf<Prevision>()
    private lateinit var recycler: RecyclerView

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

    @SuppressLint("MissingPermission")
    private fun activateLocationServices() {
        val locationRequest = LocationRequest.create()

        locationRequest.priority = LocationRequest.PRIORITY_LOW_POWER

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, null)
        sendWeatherRequest()
    }

    @SuppressLint("MissingPermission")
    private fun sendWeatherRequest() {
        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                // Building the weather request query
                val query = Query.Builder()
                        .setLat(location.latitude)
                        .setLong(location.longitude)
                        .setTemperatureUnits(Query.TemperatureUnits.CELSIUS)
                        .build()

                Log.d("FUNSHINE", query.getUrl())

                // Setting up the HTTP request as a GET method
                val jsonObjReq = JsonObjectRequest(Request.Method.GET, query.getUrl(), null,
                        Response.Listener<JSONObject> { response ->
                            Log.d("FUNSHINE", "Res: $response")

                            try {
                                val city = City(response.getJSONObject("city"))
                                val list = response.getJSONArray("list")
                                val count = response.getInt("cnt")
                                (0 until count).mapTo(previsions) { Prevision(list.getJSONObject(it)) }

                                Log.d("FUNSHINE", "$city")

                                // Updating main layout
                                val firstPrevision = previsions.first()

                                main_date.text = firstPrevision.dateString
                                main_city.text = city.full_desc
                                main_weather_desc.text = firstPrevision.sky.description
                                main_maxTemp.text = firstPrevision.temperature.max
                                main_minTemp.text = firstPrevision.temperature.min
                                main_logo.setImageResource(resources.getIdentifier(firstPrevision.sky.icon, null, packageName))

                                // Notify recycler view
                                recycler.adapter.notifyDataSetChanged()

                            } catch (e: JSONException) {
                                Log.e("FUNSHINE", e.localizedMessage)
                            }
                        },
                        Response.ErrorListener { error ->
                            Log.d("FUNSHINE", "Err: ${error.localizedMessage}")
                        })

                // Processing the HTTP request
                Volley.newRequestQueue(this).add(jsonObjReq)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_LOCATION_CODE -> {
                if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    activateLocationServices()
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

        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d("FUNSHINE", "${locationResult.locations.last()}")
            }
        }

        recycler = findViewById(R.id.main_previsions)
        recycler.setHasFixedSize(true)

        recycler.adapter = WeatherAdapter(previsions)
        recycler.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
    }

    override fun onResume() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION_CODE)
        } else {
            activateLocationServices()
        }
        super.onResume()
    }

    override fun onStop() {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback)
        super.onStop()
    }
}

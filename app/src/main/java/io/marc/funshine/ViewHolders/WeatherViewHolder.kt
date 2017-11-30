package io.marc.funshine.ViewHolders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import io.marc.funshine.Model.Prevision
import io.marc.funshine.R

/**
 *  Project Funshine
 *  Created by Marc on 30/11/2017.
 */

class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val day = view.findViewById<TextView>(R.id.card_day)
    val desc = view.findViewById<TextView>(R.id.card_description)
    val tempMax = view.findViewById<TextView>(R.id.card_maxTemp)
    val tempMin = view.findViewById<TextView>(R.id.card_minTemp)
    val weatherThumb = view.findViewById<ImageView>(R.id.card_weatherThumb)

    fun updateUI(prevision: Prevision) {
        day.text = prevision.dateString
        desc.text = prevision.sky.description
        tempMax.text = prevision.temperature.max
        tempMin.text = prevision.temperature.min
        weatherThumb.setImageResource(
                weatherThumb.resources.getIdentifier(prevision.sky.miniIcon,
                        null, weatherThumb.context.packageName))
    }
}
package io.marc.funshine.Adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import io.marc.funshine.Model.Prevision
import io.marc.funshine.R
import io.marc.funshine.ViewHolders.WeatherViewHolder

/**
 *  Project Funshine
 *  Created by Marc on 30/11/2017.
 */
class WeatherAdapter(val previsions: ArrayList<Prevision>) : RecyclerView.Adapter<WeatherViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = WeatherViewHolder(
            LayoutInflater.from(parent!!.context).inflate(R.layout.card_prevision, parent, false)
    )

    override fun onBindViewHolder(holder: WeatherViewHolder?, position: Int) {
        holder!!.updateUI(previsions[position])
        holder.itemView.setOnClickListener { v ->
            Log.d("FUNSHINE", "Clicked on ${v.id}")
        }
    }

    override fun getItemCount() = previsions.size
}
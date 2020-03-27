package com.cleteci.redsolidaria.ui.components

import android.widget.TextView
import android.R.attr.name
import android.app.Activity
import android.content.Context
import android.view.View
import com.cleteci.redsolidaria.R
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.GoogleMap


class CustomInfoWindowGoogleMap(private val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {

        val view = (context as Activity).layoutInflater
            .inflate(R.layout.item_detail_map, null)
        return view
    }

    override fun getInfoContents(marker: Marker): View? {


        return null
    }


}
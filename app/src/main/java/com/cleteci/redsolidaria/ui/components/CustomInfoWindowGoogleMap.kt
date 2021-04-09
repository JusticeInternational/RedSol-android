package com.cleteci.redsolidaria.ui.components

import android.app.Activity
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.cleteci.redsolidaria.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_organization_profile.*


class CustomInfoWindowGoogleMap(private val activity: Activity) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        val rootView: View = (activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.item_detail_map, null)
        val imgService: ImageView = rootView.findViewById(R.id.imgService)
        val tvServiceTitle: TextView = rootView.findViewById(R.id.tvServicesTitle)
        val tvOrganizationName: TextView = rootView.findViewById(R.id.tvOrganizationName)
        val link: TextView = rootView.findViewById(R.id.tvSeeDetails)

        val markerInfo: MarkerInfo = marker.tag as MarkerInfo
        imgService.setImageDrawable(AppCompatResources.getDrawable(activity,markerInfo.serviceIcon))
        tvServiceTitle.text = markerInfo.serviceTitle
        tvOrganizationName.text = markerInfo.organizationName
        link.text = Html.fromHtml(activity.getString(R.string.see_details))
        return rootView
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    class MarkerInfo(
        val serviceTitle: String,
        val organizationName: String,
        val organizationId: String,
        val serviceIcon: Int)
}
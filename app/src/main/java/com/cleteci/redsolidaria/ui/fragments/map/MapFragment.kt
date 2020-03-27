package com.cleteci.redsolidaria.ui.fragments.map


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Resourse
import com.cleteci.redsolidaria.models.ResourseCategory
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourseAdapter
import com.cleteci.redsolidaria.ui.adapters.ResourseCategoryAdapter
import com.cleteci.redsolidaria.ui.fragments.advancedsearch.AdvancedSearchFragment
import com.cleteci.redsolidaria.ui.fragments.resourcesByCity.ResourcesByCityContract

import com.cleteci.redsolidaria.ui.fragments.servicedetail.ServiceDetailFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.maps.*
import javax.inject.Inject

import com.cleteci.redsolidaria.ui.components.CustomInfoWindowGoogleMap

import com.google.android.gms.maps.CameraUpdateFactory

import android.widget.TextView
import android.content.Context.LAYOUT_INFLATER_SERVICE
import androidx.core.content.ContextCompat.getSystemService

import android.content.Context
import android.graphics.Bitmap
import android.app.Activity
import android.graphics.Canvas
import android.util.DisplayMetrics

import androidx.annotation.DrawableRes
import com.google.android.gms.maps.model.*
import android.R.layout
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import com.cleteci.redsolidaria.ui.base.BaseFragment


class MapFragment : BaseFragment(), MapContract.View, OnMapReadyCallback, ResourseCategoryAdapter.onItemClickListener,
    GoogleMap.OnInfoWindowClickListener {
    var mListRecyclerView: RecyclerView? = null
    var mAdapter: ResourseCategoryAdapter? = null
    private val listCategory = ArrayList<ResourseCategory>()
    private lateinit var mMap: GoogleMap
    var mapView: MapView? = null


    @Inject
    lateinit var presenter: MapContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): MapFragment {
        return MapFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_map, container, false)

        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.advancedContainer, AdvancedSearchFragment().newInstance())
            .commit()


        mListRecyclerView = rootView?.findViewById(R.id.rvResourses);
        mListRecyclerView?.setLayoutManager(LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        // only create and set a new adapter if there isn't already one

        mAdapter = ResourseCategoryAdapter(activity?.applicationContext, listCategory, this, 3)
        mListRecyclerView?.setAdapter(mAdapter);

        MapsInitializer.initialize(activity)

        mapView = rootView.findViewById(R.id.map)
        mapView?.onCreate(savedInstanceState)

        when (GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity)) {

            ConnectionResult.SUCCESS -> {
                mapView = rootView.findViewById(R.id.map)
                mapView?.onCreate(savedInstanceState)
                if (mapView != null) {
                    mapView?.getMapAsync(this)
                }
            }
            ConnectionResult.SERVICE_MISSING -> Toast.makeText(
                context,
                "SERVICE MISSING",
                Toast.LENGTH_SHORT
            ).show()
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> Toast.makeText(
                context,
                "UPDATE REQUIRED",
                Toast.LENGTH_SHORT
            ).show()
            ConnectionResult.SERVICE_INVALID -> Toast.makeText(
                context,
                "The version of Google play services installed on this device is not authentic",
                Toast.LENGTH_SHORT
            ).show()
            else -> Toast.makeText(
                context,
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity),
                Toast.LENGTH_SHORT
            ).show()
        }


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }


    private fun injectDependency() {
        val aboutComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        aboutComponent.inject(this)
    }

    override fun init() {

    }

    private fun initView() {
        //presenter.loadMessage()
    }

    override fun onResume() {
        super.onResume()
        if (mapView != null) {
            mapView?.onResume()

        }

        (activity as MainActivity).setTextToolbar(
            getText(R.string.map).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mapView != null) {
            mapView?.onDestroy()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (mapView != null) {
            mapView?.onLowMemory()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.setMaxZoomPreference(20f)
        mMap.setMinZoomPreference(15f)
        mMap.setOnInfoWindowClickListener(this)

        val marker = LatLng(41.4036299, 2.1743558)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))

        val marker2 = LatLng(41.4036289, 2.1743568)

        createMarker(marker2, R.drawable.ic_justice)

        val marker4 = LatLng(41.4040338, 2.1751424)

        createMarker(marker4, R.drawable.ic_emergency)

        val marker5 = LatLng(41.4065105, 2.178318)

        createMarker(marker5, R.drawable.ic_job)

        val marker6 = LatLng(41.4033097, 2.1789857)

        createMarker(marker6, R.drawable.ic_transp)

        val marker7 = LatLng(41.4016924, 2.1764415)

        createMarker(marker7, R.drawable.ic_education)

        val marker8 = LatLng(41.4017736, 2.173356)

        createMarker(marker8, R.drawable.ic_emergency)

        presenter.loadData()

    }


    fun createDrawableFromView(context: Context, view: View): Bitmap {

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay
            .getMetrics(displayMetrics)
        view!!.setLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        view.layout(
            0, 0, displayMetrics.widthPixels,
            displayMetrics.heightPixels
        )
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(
            view.getMeasuredWidth(),
            view.getMeasuredHeight(), Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    override fun onInfoWindowClick(p0: Marker?) {
        activity!!.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container1, ServiceDetailFragment().newInstance(), ServiceDetailFragment.TAG)
            .commit()
    }

    fun createMarker(sydney: LatLng, image: Int) {

        val viewMarker: View = (context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.item_marker,
            null
        )
        val myImage: ImageView = viewMarker.findViewById(R.id.imageview)

        val tvName: TextView = viewMarker.findViewById(R.id.tvNameM)

        tvName.setText("salud")

        myImage.setImageResource(image)

        val bmp: Bitmap = createDrawableFromView(context!!, viewMarker)

        val markerOptions = MarkerOptions()
        markerOptions.position(sydney)
            .title("Justice International")
            .icon(BitmapDescriptorFactory.fromBitmap(bmp))

        val customInfoWindow = CustomInfoWindowGoogleMap(activity!!)

        mMap.setInfoWindowAdapter(customInfoWindow)

        mMap.addMarker(markerOptions)

    }

    override fun loadDataSuccess(list: List<ResourseCategory>) {
        listCategory.clear()
        listCategory.addAll(list)
        mAdapter?.notifyDataSetChanged()

    }


    companion object {
        val TAG: String = "MapFragment"
    }

    override fun itemDetail(postId: Int) {
        mMap.clear()

        if (postId == 0) {
            val marker4 = LatLng(41.4040338, 2.1751424)
            createMarker(marker4, R.drawable.ic_emergency)
            val marker8 = LatLng(41.4017736, 2.173356)
            createMarker(marker8, R.drawable.ic_emergency)
        } else if (postId == 1) {
            val marker7 = LatLng(41.4016924, 2.1764415)
            createMarker(marker7, R.drawable.ic_education)
        } else if (postId == 2) {
            val marker5 = LatLng(41.4065105, 2.178318)
            createMarker(marker5, R.drawable.ic_job)
        } else if (postId == 3) {
            val marker6 = LatLng(41.4033097, 2.1789857)
            createMarker(marker6, R.drawable.ic_transp)
        } else if (postId == 4) {
            val marker2 = LatLng(41.4036289, 2.1743568)
            createMarker(marker2, R.drawable.ic_justice)
        }

    }


}

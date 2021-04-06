package com.cleteci.redsolidaria.ui.fragments.map


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.data.LocalDataForUITest.getOrganizationsList
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.CategoriesAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.components.CustomInfoWindowGoogleMap
import com.cleteci.redsolidaria.ui.fragments.advancedsearch.AdvancedSearchFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject


class MapFragment : BaseFragment(), MapContract.View, OnMapReadyCallback, CategoriesAdapter.OnItemClickListener,
    GoogleMap.OnInfoWindowClickListener {

    @Inject
    lateinit var presenter: MapContract.Presenter
    private lateinit var mMap: GoogleMap
    private var mAdapter: CategoriesAdapter? = null
    private val listCategory = ArrayList<Category>()

    fun newInstance(): MapFragment {
        return MapFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.advancedContainer, AdvancedSearchFragment().newInstance())
            .commit()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()

        rvCategories.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = CategoriesAdapter(requireContext(), listCategory, this, 3,  false)
        rvCategories.adapter = mAdapter

        MapsInitializer.initialize(activity)

        mapView?.onCreate(savedInstanceState)
        when (GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity)) {
            ConnectionResult.SUCCESS -> {
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

    override fun onResume() {
        super.onResume()
        if (mapView != null) {
            mapView?.onResume()
        }
        (activity as MainActivity).setTextToolbar(
            getText(R.string.map).toString(), activity!!.resources.getColor(R.color.colorWhite))
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
        mMap.setOnInfoWindowClickListener(this)
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(25.7617,-80.1918)) // Sets the center of the map to Miami //LatLng(37.09, -97.02) // United States of America as default
            .zoom(11f) // Sets the zoom
            .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        if (ContextCompat.checkSelfPermission(
                requireContext(), USER_LOCATION_ACCESS_PERMISSION_STRING) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            // Should we show an explanation?
            if (!shouldShowRequestPermissionRationale(USER_LOCATION_ACCESS_PERMISSION_STRING)) {
                // No explanation needed, we can request the permission.
                requestPermissions(USER_LOCATION_ACCESS_PERMISSION_ARRAY, USER_LOCATION_ACCESS_PERMISSION_REQUEST_ID)
            }
        }
        mMap.uiSettings.isMyLocationButtonEnabled = false

        for (organization in getOrganizationsList()) {
            var space = 0.0
            for (service in organization.servicesList!!) {
                createMarker(service, organization.name, LatLng(organization.lat + space, organization.lng))
                space += 0.05
            }
        }

        presenter.loadData()
    }

    private fun createMarker(service: Service, organizationName: String, latLng: LatLng) {
        val viewMarker: View = (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.item_marker, null)

        val imgMarker: ImageView = viewMarker.findViewById(R.id.imgMarker)
        imgMarker.setImageResource(service.category.iconId)
        val bmp: Bitmap = createDrawableFromView(requireContext(), viewMarker)
        mMap.setInfoWindowAdapter(CustomInfoWindowGoogleMap(activity!!))

       val marker = mMap.addMarker(MarkerOptions()
            .position(latLng)
            .title(service.name)
            .icon(BitmapDescriptorFactory.fromBitmap(bmp)))
        marker.tag = CustomInfoWindowGoogleMap.MarkerInfo(
            service.name, organizationName, service.category.iconId)
    }

    private fun createDrawableFromView(context: Context, view: View): Bitmap {
        val pxH = context.resources.getDimensionPixelSize(R.dimen.height_marker)
        val pxW = context.resources.getDimensionPixelSize(R.dimen.width_marker)
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layout(0, 0, pxH, pxH)
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(pxH, pxH, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        view.background?.draw(canvas)
        view.draw(canvas)
        return bitmap
    }

    override fun onInfoWindowClick(p0: Marker?) {
        (activity as MainActivity).openOrganizationProfile()
    }

    override fun loadDataSuccess(list: List<Category>) {
        listCategory.clear()
        listCategory.addAll(list)
        mAdapter?.notifyDataSetChanged()

    }

    override fun init() {}

    override fun clickScanCategory(position: Int) {
        mMap.clear()
        for (organization in getOrganizationsList()) {
            var space = 0.0
            for (service in organization.servicesList!!) {
                if (service.category.id == listCategory[position].id) {
                    createMarker(service, organization.name, LatLng(organization.lat + space, organization.lng))
                    space += 0.05
                }
            }
        }
    }

    override fun scanNoUserCategory(position: Int) {}

    override fun itemDetail(categoryId: Int) {
        mMap.clear()
    }

    companion object {
        const val TAG: String = "MapFragment"
        const val USER_LOCATION_ACCESS_PERMISSION_REQUEST_ID = 11
        const val USER_LOCATION_ACCESS_PERMISSION_STRING =
            Manifest.permission.ACCESS_FINE_LOCATION
        val USER_LOCATION_ACCESS_PERMISSION_ARRAY =
            arrayOf(USER_LOCATION_ACCESS_PERMISSION_STRING)
    }
}

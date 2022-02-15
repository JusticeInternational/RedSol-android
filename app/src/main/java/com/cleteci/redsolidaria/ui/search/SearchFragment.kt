package com.cleteci.redsolidaria.ui.search


import android.Manifest
import android.content.Context
import android.content.Intent
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
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.data.LocalDataForUITest.getOrganizationsList
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.models.Organization
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.CategoriesSearchAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.components.CustomInfoWindowGoogleMap
import com.cleteci.redsolidaria.ui.fragments.advancedsearch.AdvancedSearchFragment
import com.cleteci.redsolidaria.ui.search.SearchItemsActivity.Companion.ID_LOCATION_LAT
import com.cleteci.redsolidaria.ui.search.SearchItemsActivity.Companion.ID_LOCATION_LNG
import com.cleteci.redsolidaria.ui.search.SearchItemsActivity.Companion.ID_LOCATION_TEXT
import com.cleteci.redsolidaria.ui.search.SearchItemsActivity.Companion.ID_QUERY
import com.cleteci.redsolidaria.ui.search.SearchItemsActivity.Companion.SEARCH_REQUEST_CODE
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject


class SearchFragment : BaseFragment(),
    SearchContract.View, OnMapReadyCallback, CategoriesSearchAdapter.OnItemClickListener,
    GoogleMap.OnInfoWindowClickListener, ServicesSearchAdapter.OnItemClickListener {

    @Inject
    lateinit var presenter: SearchContract.Presenter
    private lateinit var mMap: GoogleMap
    private var mAdapter: CategoriesSearchAdapter? = null
    private var mAdapterServices: ServicesSearchAdapter? = null
    private val listCategory = ArrayList<Category>()
    private val listServices = ArrayList<ServicesSearchAdapter.ServiceSearch>()
    private var currentViewType = MAP_VIEW

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
        mAdapter = CategoriesSearchAdapter(requireContext(), listCategory, this)
        rvCategories.adapter = mAdapter

        rvServices.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        mAdapterServices = ServicesSearchAdapter(requireContext(), listServices, this)
        rvServices.adapter = mAdapterServices

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
        (activity as MainActivity).setSearchButton()
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
            .zoom(12f) // Sets the zoom
            .build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                USER_LOCATION_ACCESS_PERMISSION_STRING
            ) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            val locationButton = (mapView.findViewById<View>("1".toInt()).parent as View).findViewById<View>("2".toInt())
            val rlp: RelativeLayout.LayoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 30)
        } else {
            // Should we show an explanation?
            if (!shouldShowRequestPermissionRationale(USER_LOCATION_ACCESS_PERMISSION_STRING)) {
                // No explanation needed, we can request the permission.
                requestPermissions(
                    USER_LOCATION_ACCESS_PERMISSION_ARRAY,
                    USER_LOCATION_ACCESS_PERMISSION_REQUEST_ID
                )
            }
        }

        for (organization in getOrganizationsList()) {
            var space = 0.0
            for (service in organization.servicesList!!) {
                createMarker(service, organization, LatLng(organization.lat + space, organization.lng))
                listServices.add(
                    ServicesSearchAdapter.ServiceSearch(service, organization.id, organization.name))
                space += 0.05
            }
        }

        presenter.loadData()
    }

    private fun createMarker(service: Service, organization: Organization, latLng: LatLng) {
        val viewMarker: View = (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.item_marker, null)

        val imgMarker: ImageView = viewMarker.findViewById(R.id.imgOrganization)
        imgMarker.setImageResource(service.category!!.iconId)
        val bmp: Bitmap = createDrawableFromView(requireContext(), viewMarker)
        mMap.setInfoWindowAdapter(CustomInfoWindowGoogleMap(activity!!))

       val marker = mMap.addMarker(MarkerOptions()
            .position(latLng)
            .title(service.name)
            .icon(BitmapDescriptorFactory.fromBitmap(bmp)))
        marker.tag = service.name?.let {
            CustomInfoWindowGoogleMap.MarkerInfo(
                it, organization.name, organization.id, service.category.iconId)
        }
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

    override fun onInfoWindowClick(marker: Marker?) {
        (activity as MainActivity).openOrganizationProfile((marker?.tag as CustomInfoWindowGoogleMap.MarkerInfo).organizationId)
    }

    override fun onServiceSearchClicked(position: Int) {
        (activity as MainActivity).openOrganizationProfile(listServices[position].organizationId)

    }

    override fun loadDataSuccess(list: List<Category>) {
        listCategory.clear()
        listCategory.add(Category("-1","Todas", -1))
        listCategory.addAll(list)
        mAdapter?.notifyDataSetChanged()

    }

    override fun clickScanCategory(position: Int) {
        val category = listCategory[position]
        if (category.id == "-1") {// All Categories
            (activity as MainActivity).setSearchLabel(getString(R.string.search))
        } else {
            (activity as MainActivity).setSearchLabel(category.name)
        }

        filterMapList(category.id)
    }

    private fun filterMapList(categoryId: String) {
        mMap.clear()
        listServices.clear()
        for (organization in getOrganizationsList()) {
            var space = 0.0
            for (service in organization.servicesList!!) {
                if (categoryId == "-1") {// All Categories
                    createMarker(service, organization, LatLng(organization.lat + space, organization.lng))
                    listServices.add(
                        ServicesSearchAdapter.ServiceSearch(service, organization.id, organization.name))
                    space += 0.05
                } else if (service.category!!.id == categoryId) {
                    createMarker(service, organization, LatLng(organization.lat + space, organization.lng))
                    listServices.add(
                        ServicesSearchAdapter.ServiceSearch(service, organization.id, organization.name))
                    space += 0.05
                }
            }
            mAdapterServices!!.notifyDataSetChanged()
        }
    }

    override fun init() {}
    fun onClickMapListButton(icon: ImageView) {
         if (currentViewType == MAP_VIEW) {
             mapView.visibility = View.GONE
             rvServices.visibility = View.VISIBLE
             currentViewType = LIST_VIEW
             icon.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_map))
        } else {
             mapView.visibility = View.VISIBLE
             rvServices.visibility = View.GONE
             currentViewType = MAP_VIEW
             icon.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_list))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == SEARCH_REQUEST_CODE) {
            if (data != null) {
                val categoryId = data.getStringExtra(ID_QUERY)
                val locationText = data.getStringExtra(ID_LOCATION_TEXT)

                for (category in listCategory) {
                    if (category.id == categoryId) {
                        (activity as MainActivity).setSearchLabel(category.name + ", " + locationText)
                    }
                }
                filterMapList(categoryId)

                val lat = data.getDoubleExtra(ID_LOCATION_LAT, 0.0)
                val lng = data.getDoubleExtra(ID_LOCATION_LNG, 0.0)
                if (lat != 0.0 && lng != 0.0) {
                    val cameraPosition = CameraPosition.Builder()
                        .target(LatLng(lat,lng))
                        .zoom(12f) // Sets the zoom
                        .build()

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }
            }
        }
    }

    companion object {
        const val TAG: String = "MapFragment"
        const val MAP_VIEW = 0
        const val LIST_VIEW = 1
        const val USER_LOCATION_ACCESS_PERMISSION_REQUEST_ID = 11
        const val USER_LOCATION_ACCESS_PERMISSION_STRING =
            Manifest.permission.ACCESS_FINE_LOCATION
        val USER_LOCATION_ACCESS_PERMISSION_ARRAY =
            arrayOf(USER_LOCATION_ACCESS_PERMISSION_STRING)
    }
}

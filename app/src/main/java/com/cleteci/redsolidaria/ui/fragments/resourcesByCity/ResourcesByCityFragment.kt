package com.cleteci.redsolidaria.ui.fragments.resourcesByCity


import android.os.Bundle
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
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourseAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.fragments.advancedsearch.AdvancedSearchFragment
import com.cleteci.redsolidaria.ui.fragments.servicedetail.ServiceDetailFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


class ResourcesByCityFragment : BaseFragment(), ResourcesByCityContract.View, OnMapReadyCallback,
    ResourseAdapter.onItemClickListener {


    var mListRecyclerView: RecyclerView? = null
    var mAdapter: ResourseAdapter? = null
    private val listCategory = ArrayList<Resourse>()

    private lateinit var mMap: GoogleMap

    var mapView: MapView? = null

    @Inject
    lateinit var presenter: ResourcesByCityContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): ResourcesByCityFragment {
        return ResourcesByCityFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_resourses_by_city, container, false)

        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.advancedContainer, AdvancedSearchFragment().newInstance())
            .commit()


        mListRecyclerView = rootView?.findViewById(R.id.rvResourses);
        mListRecyclerView?.setLayoutManager(LinearLayoutManager(getActivity()));

        // only create and set a new adapter if there isn't already one
        //if (mAdapter == null) {
        mAdapter = ResourseAdapter(activity?.applicationContext, listCategory, this, 1)
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
            //  mapaPresenter.loadPines(String.valueOf(ubicacionUser.longitude), String.valueOf(ubicacionUser.latitude));
        }

        (activity as MainActivity).setTextToolbar(
            getText(R.string.search).toString(),
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

        val marker = LatLng(41.4036299, 2.1743558)

        mMap.addMarker(MarkerOptions().position(marker).title("Justice International"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))


        val marker2 = LatLng(41.4036289, 2.1743568)

        mMap.addMarker(MarkerOptions().position(marker2).title("Justice International"))

        val marker3 = LatLng(41.404115, 2.1738613)

        mMap.addMarker(MarkerOptions().position(marker3).title("Justice International"))

        val marker4 = LatLng(41.4040338, 2.1751424)

        mMap.addMarker(MarkerOptions().position(marker4).title("Justice International"))

        val marker5 = LatLng(41.4065105, 2.178318)

        mMap.addMarker(MarkerOptions().position(marker5).title("Justice International"))

        val marker6 = LatLng(41.4033097, 2.1789857)

        mMap.addMarker(MarkerOptions().position(marker6).title("Justice International"))

        val marker7 = LatLng(41.4016924, 2.1764415)

        mMap.addMarker(MarkerOptions().position(marker7).title("Justice International"))

        val marker8 = LatLng(41.4017736, 2.173356)

        mMap.addMarker(MarkerOptions().position(marker8).title("Justice International"))

        val marker9 = LatLng(41.4048323, 2.4048323)

        mMap.addMarker(MarkerOptions().position(marker9).title("Justice International"))

        val marker10 = LatLng(41.4063413, 2.1713802)

        mMap.addMarker(MarkerOptions().position(marker10).title("Justice International"))

        val marker11 = LatLng(41.4073794, 2.1707575)

        mMap.addMarker(MarkerOptions().position(marker11).title("Justice International"))

        presenter.loadData()


    }

    override fun loadDataSuccess(list: List<Resourse>) {

        listCategory.clear()
        listCategory.addAll(list)

        mAdapter?.notifyDataSetChanged()
    }

    override fun clickDetailResource(postId: String) {

        activity!!.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container1, ServiceDetailFragment().newInstance(), ServiceDetailFragment.TAG)
            .commit()

    }


    companion object {
        val TAG: String = "RBCFragment"
    }

}

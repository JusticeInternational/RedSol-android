package com.cleteci.redsolidaria.ui.fragments.advancedsearch


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.ResourseCategory
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourseCategoryAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.fragments.basicsearch.BasicSearchFragment.Companion.TAG
import com.cleteci.redsolidaria.ui.fragments.resourcesByCity.ResourcesByCityFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.schibstedspain.leku.*
import com.schibstedspain.leku.locale.SearchZoneRect
import javax.inject.Inject


class AdvancedSearchFragment : BaseFragment(), AdvancedSearchContract.View,
    ResourseCategoryAdapter.onItemClickListener {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    protected var mLastLocation: Location? = null
    var mListRecyclerView: RecyclerView? = null
    var lyLocation: LinearLayout? = null
    var tvResult: TextView? = null
    var lyContainer: LinearLayout? = null
    var tvMyLocation: TextView? = null
    var searchView: SearchView? = null
    var mAdapter: ResourseCategoryAdapter? = null
    private val listCategory = ArrayList<ResourseCategory>()

    @Inject
    lateinit var presenter: AdvancedSearchContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): AdvancedSearchFragment {
        return AdvancedSearchFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_advanced_search, container, false)
        mListRecyclerView = rootView?.findViewById(R.id.recyclerViewAdvanced);
        tvResult = rootView?.findViewById(R.id.tvResult);
        tvMyLocation = rootView?.findViewById(R.id.tvMyLocation);
        lyContainer = rootView?.findViewById(R.id.lyContainer);
        searchView = rootView?.findViewById(R.id.searchView);
        searchView!!.setIconified(false)
        var imm: InputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm != null) {
            imm.showSoftInput(view, 0)
        }
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                doSearch()

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })



        searchView?.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                listCategory.clear()
                lyContainer?.visibility = View.GONE
                mAdapter?.notifyDataSetChanged()
                searchView?.setQuery("", false)
                return false
            }
        })

        searchView?.clearFocus(); // close the keyboard on load

        lyLocation = rootView?.findViewById(R.id.lyLocation);
        lyLocation!!.setOnClickListener {
            val locationPickerIntent = LocationPickerActivity.Builder()
                .withLocation(41.4036299, 2.1743558)
                .withGeolocApiKey(getString(R.string.map_key))
                .withSearchZone("es_ES")
                .build(context!!)

            startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE)
        }
        mListRecyclerView?.setLayoutManager(LinearLayoutManager(getActivity()))
        mAdapter = ResourseCategoryAdapter(activity?.applicationContext, listCategory, this, 2)
        mListRecyclerView?.setAdapter(mAdapter)
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

    fun doSearch() {
        val query = searchView?.getQuery().toString()
        if (query != null && !query.isEmpty()) {

            lyContainer?.visibility = View.VISIBLE
            presenter.search(query)
        }

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
        //presenter.loadData()

    }


    public override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    override fun loadDataSuccess(list: List<ResourseCategory>) {
        listCategory.clear()
        listCategory.addAll(list)
        //Log.d("sasasa", "sassasasasa2");
        mAdapter?.notifyDataSetChanged()

        //}


    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     *
     *
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {


        mFusedLocationClient!!.lastLocation
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result
                }
            }
    }


    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            activity!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")



            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                View.OnClickListener {
                    // Request permission
                    startLocationPermissionRequest()
                })

        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest()
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation()
            } else {
                // Permission denied.


            }
        }
    }

    private fun showSnackbar(
        mainTextStringId: Int, actionStringId: Int,
        listener: View.OnClickListener
    ) {

        Toast.makeText(activity, getString(mainTextStringId), Toast.LENGTH_LONG).show()
    }

    override fun itemDetail(postId: Int) {
        activity!!.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            // .setCustomAnimations(AnimType.FADE.getAnimPair().first, AnimType.FADE.getAnimPair().second)
            .replace(R.id.container1, ResourcesByCityFragment().newInstance(), ResourcesByCityFragment.TAG)
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {


            if (requestCode == MAP_BUTTON_REQUEST_CODE) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)

                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)

                val address = data.getStringExtra(LOCATION_ADDRESS)

                tvMyLocation?.setText(address);

                val fullAddress = data.getParcelableExtra<Address>(ADDRESS)
                if (fullAddress != null) {
                    Log.d("FULL ADDRESS****", fullAddress.toString())
                }

            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED")
        }
    }


    companion object {
        val TAG: String = "AdvancedFragment"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        private val MAP_BUTTON_REQUEST_CODE = 57
    }

}

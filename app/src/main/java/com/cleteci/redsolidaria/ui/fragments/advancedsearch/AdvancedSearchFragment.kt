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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.ui.adapters.CategoriesAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.fragments.resourcesResult.ResourcesResultFragment
import com.cleteci.redsolidaria.util.LocationResources
import com.cleteci.redsolidaria.util.showAlert
import com.google.android.gms.location.FusedLocationProviderClient
import com.schibstedspain.leku.*
import javax.inject.Inject


class AdvancedSearchFragment : BaseFragment(), AdvancedSearchContract.View,
    CategoriesAdapter.OnItemClickListener {


    protected var mLastLocation: Location? = null
    var mListRecyclerView: RecyclerView? = null
    var lyLocation: LinearLayout? = null
    var tvResult: TextView? = null
    var lyContainer: LinearLayout? = null
    var tvMyLocation: TextView? = null
    var searchView: SearchView? = null
    var mAdapter: CategoriesAdapter? = null
    private val listCategory = ArrayList<Category>()
    private lateinit var locationResources: LocationResources

    @Inject
    lateinit var presenter: AdvancedSearchContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): AdvancedSearchFragment {
        return AdvancedSearchFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
        locationResources = LocationResources(requireContext(), requireActivity())
        locationResources.currentLocation.observe(this,
            androidx.lifecycle.Observer { currentLocation: Location ->
                val locationPickerIntent = LocationPickerActivity.Builder()
                    .withLocation(currentLocation.latitude, currentLocation.longitude)
                    .withGeolocApiKey(getString(R.string.google_maps_key))
                    .withSearchZone("es_ES")
                    .build(requireContext())
                startActivityForResult(
                    locationPickerIntent,
                    MAP_BUTTON_REQUEST_CODE
                )
            })
    }

    @SuppressLint("MissingPermission")
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
        var imm: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm != null && view != null) {
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
            if (locationResources.isLocationEnabled(requireContext())) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    locationResources.requestLocationPermission()

                } else {
                    locationResources.loadLocation()
                }
            } else {
                showAlert(
                    requireContext(),
                    R.drawable.ic_error,
                    getString(R.string.location_on),
                    getString(R.string.ok)
                )
            }
        }
        mListRecyclerView?.setLayoutManager(LinearLayoutManager(getActivity()))
        mAdapter = CategoriesAdapter(activity?.applicationContext, listCategory, this, 2, false)
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
    }

    override fun loadDataSuccess(list: List<Category>) {
        activity?.runOnUiThread(Runnable {
            listCategory.clear()
            listCategory.addAll(list)
            mAdapter?.notifyDataSetChanged()
        })
    }




    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
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



    private fun showSnackbar(
        mainTextStringId: Int, actionStringId: Int,
        listener: View.OnClickListener
    ) {

        Toast.makeText(activity, getString(mainTextStringId), Toast.LENGTH_LONG).show()
    }

    override fun itemDetail(postId: Int) {
        requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.container_fragment,
                ResourcesResultFragment.newInstance(this.listCategory[postId], "asdf"),
                ResourcesResultFragment.TAG
            )
            .commit()
    }

    override fun clickScanCategory(postId: Int) {


    }

    override fun scanNoUserCategory(position: Int) {

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

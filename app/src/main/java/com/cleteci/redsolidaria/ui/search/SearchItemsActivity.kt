package com.cleteci.redsolidaria.ui.search

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.data.LocalDataForUITest.getCategoriesList
import com.cleteci.redsolidaria.ui.fragments.createOrganization.CreateOrganizationFragment
import com.cleteci.redsolidaria.util.LocationResources
import com.cleteci.redsolidaria.util.showAlert
import com.schibstedspain.leku.*
import kotlinx.android.synthetic.main.activity_search.*


class SearchItemsActivity : AppCompatActivity(), SearchItemsAdapter.OnItemClickListener {

    private var suggestionsList: ArrayList<SearchItemsAdapter.SearchItem> = ArrayList()
    private lateinit var adapter: SearchItemsAdapter
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var locationResources: LocationResources

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        buildSuggestionsList()
        setUpUI()
        locationResources = LocationResources(this, this)
        locationResources.currentLocation.observe(this,
            androidx.lifecycle.Observer { currentLocation: Location ->
                val locationPickerIntent = LocationPickerActivity.Builder()
                    .withLocation(currentLocation.latitude, currentLocation.longitude)
                    .withGeolocApiKey(getString(R.string.google_maps_key))
                    .withSearchZone("es_ES")
                    .build(this)
                startActivityForResult(
                    locationPickerIntent,
                    MAP_BUTTON_REQUEST_CODE
                )
            })
    }

    private fun setUpUI() {
        categoriesList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        categoriesList.layoutManager = LinearLayoutManager(this)
        adapter = SearchItemsAdapter(this, suggestionsList, this)
        categoriesList?.adapter = adapter
        searchViewCategoryOrText.isIconified = false
        searchViewCategoryOrText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent()
                intent.putExtra(ID_QUERY, query)
                intent.putExtra(
                    ID_LOCATION_TEXT,
                    tvLocation.text.toString().replace(getString(R.string.near_to), "")
                )
                intent.putExtra(ID_LOCATION_LAT, latitude)
                intent.putExtra(ID_LOCATION_LNG, longitude)
                setResult(RESULT_OK, intent)
                finish()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText)
                return false
            }
        })
        searchViewCategoryOrText.setOnSearchClickListener {
            val intent = Intent()
            intent.putExtra(ID_QUERY, "")
            intent.putExtra(
                ID_LOCATION_TEXT,
                tvLocation.text.toString().replace(getString(R.string.near_to), "")
            )
            intent.putExtra(ID_LOCATION_LAT, latitude)
            intent.putExtra(ID_LOCATION_LNG, longitude)
            setResult(RESULT_OK, intent)
            finish()
        }
        tvLocation.text = getString(R.string.current_location_text)
        lyLocation!!.setOnClickListener {
            if (locationResources.isLocationEnabled(this)) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    locationResources.requestLocationPermission()

                } else {
                    locationResources.loadLocation()
                }
            } else {
                showAlert(
                    this,
                    R.drawable.ic_error,
                    getString(R.string.location_on),
                    getString(R.string.ok)
                )
            }
        }
    }

    private fun buildSuggestionsList() {
        for (category in getCategoriesList()) {
            val searchItem =
                SearchItemsAdapter.SearchItem(category.id, category.name, category.iconId)
            suggestionsList.add(searchItem)
        }
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<SearchItemsAdapter.SearchItem> = ArrayList()
        for (item in suggestionsList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            adapter.filterList(filteredList)
        }
    }

    override fun onSearchItemClicked(text: String) {
        val intent = Intent()
        intent.putExtra(ID_QUERY, text)
        intent.putExtra(
            ID_LOCATION_TEXT,
            tvLocation.text.toString().replace(getString(R.string.near_to), "")
        )
        intent.putExtra(ID_LOCATION_LAT, latitude)
        intent.putExtra(ID_LOCATION_LNG, longitude)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == MAP_BUTTON_REQUEST_CODE) {
                latitude = data.getDoubleExtra(LATITUDE, 0.0)
                longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                val address = data.getStringExtra(LOCATION_ADDRESS)
                val text = getString(R.string.near_to_location, address)
                tvLocation.text = text

                val fullAddress = data.getParcelableExtra<Address>(ADDRESS)
            }
        }
    }

    companion object {
        const val SEARCH_REQUEST_CODE = 0
        const val MAP_BUTTON_REQUEST_CODE = 1
        const val ID_QUERY = "id_query"
        const val ID_LOCATION_TEXT = "id_location_text"
        const val ID_LOCATION_LAT = "id_location_lat"
        const val ID_LOCATION_LNG = "id_location_lng"
        const val TAG = "SearchItemsActivity"

    }
}
package com.cleteci.redsolidaria.ui.fragments.suggestService

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.databinding.FragmentSuggestServiceBinding
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.fragments.createOrganization.CreateOrganizationFragment
import com.cleteci.redsolidaria.util.Constants
import com.cleteci.redsolidaria.util.LocationResources
import com.cleteci.redsolidaria.util.openEmailClient
import com.cleteci.redsolidaria.util.showAlert
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.schibstedspain.leku.*

class SuggestServiceFragment : BaseFragment() {
    private var _binding: FragmentSuggestServiceBinding? = null
    private val binding get() = _binding!!
    private lateinit var locationResources: LocationResources

    fun newInstance(): SuggestServiceFragment {
        return SuggestServiceFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuggestServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("MissingPermission")
    fun init() {
        binding.btCancel.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.etLocation.setOnClickListener {
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

        binding.btSend.setOnClickListener {
            if (isValidOrganizationData()) {
                val body = Html.fromHtml(
                    getString(
                        R.string.suggest_organization_body,
                        binding.etName.text.toString(),
                        binding.etWeb.text.toString(),
                        binding.etLocation.text.toString(),
                        if (binding.etPhone.text.isNullOrEmpty()) "N/A" else binding.etPhone.text.toString(),
                        binding.etDesc.text.toString()
                    )
                )
                openEmailClient(
                    requireContext(),
                    Constants.ORGANIZATION_EMAIL,
                    getString(R.string.suggest_organization_subject),
                    body
                )
            }
        }
    }

    private fun isValidOrganizationData(): Boolean {
        if (binding.etName.text.isNullOrEmpty()) {
            showError(getString(R.string.enter_valid_name))
        } else if (binding.etWeb.text.isNullOrEmpty() || !android.util.Patterns.WEB_URL.matcher(
                binding.etWeb.text.toString().trim()
            ).matches()
        ) {
            showError(getString(R.string.enter_valid_web_page))
        } else if (binding.etLocation.text.isNullOrEmpty()) {
            showError(getString(R.string.enter_valid_location))
        } else if (binding.etDesc.text.isNullOrEmpty()) {
            showError(getString(R.string.enter_some_description))
        } else {
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == MAP_BUTTON_REQUEST_CODE) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                Log.d("LATITUDE****", latitude.toString())
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                Log.d("LONGITUDE****", longitude.toString())
                val address = data.getStringExtra(LOCATION_ADDRESS)
                Log.d("ADDRESS****", address.toString())

                binding.etLocation.setText(address)

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

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getString(R.string.suggest_resources),
            activity!!.resources.getColor(R.color.colorWhite)
        )
    }

    companion object {
        const val TAG: String = "SuggestServiceFragment"
        private const val MAP_BUTTON_REQUEST_CODE = 57
    }
}
package com.cleteci.redsolidaria.ui.fragments.suggestService

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.util.Constants
import com.cleteci.redsolidaria.util.openEmailClient
import com.schibstedspain.leku.*
import kotlinx.android.synthetic.main.fragment_suggest_service.*

class SuggestServiceFragment : BaseFragment() {

    fun newInstance(): SuggestServiceFragment {
        return SuggestServiceFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_suggest_service, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init() {
        btCancel.setOnClickListener {
            activity?.onBackPressed()
        }

        etLocation.setOnClickListener {
            val locationPickerIntent = LocationPickerActivity.Builder()
                .withLocation(41.4036299, 2.1743558)
                .withGeolocApiKey(getString(R.string.google_maps_key))
                .withSearchZone("es_ES")
                .build(context!!)
            startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE)
        }

        btSend.setOnClickListener {
            if (isValidOrganizationData()) {
                val body = Html.fromHtml(getString(R.string.suggest_organization_body,
                    etName.text.toString(),
                    etWeb.text.toString(),
                    etLocation.text.toString(),
                    if (etPhone.text.isNullOrEmpty()) "N/A" else etPhone.text.toString(),
                    etDesc.text.toString()
                ))
                openEmailClient(requireContext(),
                    Constants.ORGANIZATION_EMAIL,
                    getString(R.string.suggest_organization_subject),
                    body
                )
            }
        }
    }

    private fun isValidOrganizationData(): Boolean {
        if (etName.text.isNullOrEmpty()) {
            showError(getString(R.string.enter_valid_name))
        } else if (etWeb.text.isNullOrEmpty() || !android.util.Patterns.WEB_URL.matcher(etWeb.text.toString().trim()).matches()) {
            showError(getString(R.string.enter_valid_web_page))
        } else if (etLocation.text.isNullOrEmpty()) {
            showError(getString(R.string.enter_valid_location))
        } else if (etDesc.text.isNullOrEmpty()) {
            showError(getString(R.string.enter_some_description))
        } else {
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == MAP_BUTTON_REQUEST_CODE) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                Log.d("LATITUDE****", latitude.toString())
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                Log.d("LONGITUDE****", longitude.toString())
                val address = data.getStringExtra(LOCATION_ADDRESS)
                Log.d("ADDRESS****", address.toString())

                etLocation?.setText(address);

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
            activity!!.resources.getColor(R.color.colorWhite))
    }

    companion object {
        const val TAG: String = "SuggestServiceFragment"
        private const val MAP_BUTTON_REQUEST_CODE = 57
    }
}
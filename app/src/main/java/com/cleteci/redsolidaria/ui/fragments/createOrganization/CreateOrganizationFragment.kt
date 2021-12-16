package com.cleteci.redsolidaria.ui.fragments.createOrganization

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.*
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.schibstedspain.leku.*
import kotlinx.android.synthetic.main.fragment_create_organization.*
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.util.LocationResources
import com.cleteci.redsolidaria.util.showAlert
import com.cleteci.redsolidaria.viewModels.GeneralViewModel
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel

import kotlinx.android.synthetic.main.fragment_create_organization.btCancel
import kotlinx.android.synthetic.main.fragment_create_organization.etName
import kotlinx.android.synthetic.main.fragment_create_organization.etPhone
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.UUID;

import androidx.core.app.ActivityCompat


class CreateOrganizationFragment : BaseFragment() {

    private var categoryList: ArrayList<String> = ArrayList()
    private val generalVM by viewModel<GeneralViewModel>()
    private val organizationVM by viewModel<OrganizationViewModel>()
    private lateinit var locationResources: LocationResources

    companion object {
        const val TAG: String = "CreateOrganizationFragment"
        private const val MAP_BUTTON_REQUEST_CODE = 57
    }

    fun newInstance(): CreateOrganizationFragment {
        return CreateOrganizationFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        generalVM.usedCategories.observe(this,
            androidx.lifecycle.Observer { usedCategories: ArrayList<Category> ->
                loadDataSuccess(usedCategories)
            })
        locationResources = LocationResources(requireContext(), requireActivity())
        locationResources.currentLocation.observe(this,
            androidx.lifecycle.Observer { currentLocation: Location ->
                val locationPickerIntent = LocationPickerActivity.Builder()
                    .withLocation(currentLocation.latitude, currentLocation.longitude)
                    .withGeolocApiKey(getString(R.string.google_maps_key))
                    .withSearchZone("es_ES")
                    .build(requireContext())
                startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE)
            })
        generalVM.getUsedCategories()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_create_organization, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun loadDataSuccess(list: List<Category>) {
        activity?.runOnUiThread {
            categoryList.clear()
            list.forEach {
                categoryList.add(it.name)
            }
        }
    }

    fun init() {
        btCancel.setOnClickListener {
            activity?.onBackPressed()
        }
        selectCategory.setOnClickListener {
            val adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categoryList
            )
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.select_category))
                .setAdapter(adapter) { dialog, which ->
                    selectCategory.text = categoryList[which]
                    dialog.dismiss()
                }
                .create()
                .show()
        }
        etLocation.setOnClickListener {
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

        btCreate.setOnClickListener {
            if (isValidOrganizationData()) {
                showAlert(
                    requireContext(),
                    R.drawable.ic_check_green,
                    getString(R.string.create_organization_success, etName.text),
                    getString(R.string.ok)
                )

                organizationVM.CreateOrganization(
                    OrganizationViewModel.CreateOrganizationRequest(
                        UUID.randomUUID().toString(),
                        etName.text.toString(),
                        etPhone.text.toString(),
                        etWeb.text.toString(),
                        "",
                        "",
                        etDesc.text.toString(),
                        "",
                        "",
                        ""
                    )
                )
                //  activity?.onBackPressed()
            }
        }
    }

    private fun isValidOrganizationData(): Boolean {
        if (etName.text.isNullOrEmpty()) {
            showError(getString(R.string.enter_valid_name))
        } else if (!etWeb.text.isNullOrEmpty() && !android.util.Patterns.WEB_URL.matcher(
                etWeb.text.toString().trim()
            ).matches()
        ) {
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
                val address = data.getStringExtra(LOCATION_ADDRESS)
                etLocation?.text = address
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getString(R.string.create_organization),
            requireActivity().resources.getColor(R.color.colorWhite)
        )
    }

}
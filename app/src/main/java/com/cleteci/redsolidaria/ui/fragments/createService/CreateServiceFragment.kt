package com.cleteci.redsolidaria.ui.fragments.createService

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetServiceCategoriesQuery
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.util.LocationResources
import com.cleteci.redsolidaria.util.showAlert
import com.cleteci.redsolidaria.viewModels.GeneralViewModel
import com.schibstedspain.leku.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class CreateServiceFragment : BaseFragment(), CreateServiceContract.View {

    private var btSend: Button? = null
    private var btCancel: Button? = null

    private var etName: EditText? = null
    private var etLocation: EditText? = null
    private var etCategory: EditText? = null
    private var etDesc: EditText? = null
    private var etSchedule: EditText? = null

    private val presenter: CreateServiceContract.Presenter by inject()
    private val generalVM by viewModel<GeneralViewModel>()

    private lateinit var rootView: View
    private lateinit var locationResources: LocationResources

    private var availableCategories: List<GetServiceCategoriesQuery.ServiceCategory> = emptyList()
    private var selectedCategoryId: String = ""

    fun newInstance(): CreateServiceFragment {
        return CreateServiceFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationResources = LocationResources(requireContext(), requireActivity())
        locationResources.currentLocation.observe(this) { currentLocation: Location ->
            val locationPickerIntent = LocationPickerActivity.Builder()
                .withLocation(currentLocation.latitude, currentLocation.longitude)
                .withGeolocApiKey(getString(R.string.google_maps_key))
                .withSearchZone("es_ES")
                .build(requireContext())
            startActivityForResult(
                locationPickerIntent,
                MAP_BUTTON_REQUEST_CODE
            )
        }

        generalVM.categories.observe(this) { categoriesList ->
            if (!categoriesList.isNullOrEmpty()) {
                availableCategories = categoriesList
            }
        }
        generalVM.getServiceCategories()
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_create_service, container, false)

        btCancel = rootView.findViewById(R.id.btCancel)
        btCancel?.setOnClickListener {
            (activity as? MainActivity)?.onBackPressed()
        }

        etName = rootView.findViewById(R.id.etName)
        etLocation = rootView.findViewById(R.id.etLocation)
        etCategory = rootView.findViewById(R.id.etCategory)
        etDesc = rootView.findViewById(R.id.etDesc)
        etSchedule = rootView.findViewById(R.id.etSchedule)
        btSend = rootView.findViewById(R.id.btSend)

        etCategory?.isFocusable = false
        etCategory?.isClickable = true
        etCategory?.setOnClickListener {
            showCategoryPicker()
        }

        etLocation?.setOnClickListener {
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

        btSend?.setOnClickListener {
            submitService()
        }

        return rootView
    }

    private fun showCategoryPicker() {
        if (availableCategories.isEmpty()) {
            Toast.makeText(context, "Cargando categorías, intente nuevamente...", Toast.LENGTH_SHORT).show()
            generalVM.getServiceCategories()
            return
        }

        val categoryNames = availableCategories.map { it.name().orEmpty() }.toTypedArray()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categoryNames)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.category))
            .setAdapter(adapter) { dialog, which ->
                val selected = availableCategories[which]
                selectedCategoryId = selected.id()
                etCategory?.setText(selected.name().orEmpty())
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun submitService() {
        val name = etName?.text.toString().trim()
        val desc = etDesc?.text.toString().trim()
        val orgId = BaseApp.sharedPreferences.currentOrganizationId.orEmpty()

        if (name.isEmpty()) {
            Toast.makeText(context, getString(R.string.invalid_name), Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedCategoryId.isEmpty()) {
            Toast.makeText(context, "Por favor seleccione una categoría", Toast.LENGTH_SHORT).show()
            return
        }

        if (desc.isEmpty()) {
            Toast.makeText(context, "Por favor complete la descripción", Toast.LENGTH_SHORT).show()
            return
        }

        if (orgId.isEmpty()) {
            Toast.makeText(context, "No se encontró organización activa", Toast.LENGTH_LONG).show()
            return
        }

        presenter.createService(
            name = name,
            description = desc,
            isGeneral = false,
            organizationId = orgId,
            serviceCategoryId = selectedCategoryId
        )
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

    override fun init() {}

    private fun initView() {}

    override fun onServiceLoading(show: Boolean) {
        activity?.runOnUiThread {
            btSend?.isEnabled = !show
        }
    }

    override fun onServiceCreatedSuccess(msg: String) {
        activity?.runOnUiThread {
            showSuccessDialog(msg)
        }
    }

    override fun onServiceCreatedError(msg: String) {
        activity?.runOnUiThread {
            Toast.makeText(context ?: BaseApp.instance, msg, Toast.LENGTH_LONG).show()
        }
    }

    private fun showSuccessDialog(msg: String) {
        val act = activity ?: return
        if (!isAdded || act.isFinishing) return

        val dialog = Dialog(act)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.comp_alert_succes_suggest_resource)

        val ivIcon = dialog.findViewById<ImageView>(R.id.ivIcon)
        val tvAlertMsg = dialog.findViewById<TextView>(R.id.tvAlertMsg)
        ivIcon.setImageResource(R.drawable.ic_check_green)
        tvAlertMsg.text = msg

        val yesBtn = dialog.findViewById<Button>(R.id.btCont)
        yesBtn.setOnClickListener {
            dialog.dismiss()
            (activity as? MainActivity)?.onBackPressed()
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == MAP_BUTTON_REQUEST_CODE) {
                val address = data.getStringExtra(LOCATION_ADDRESS)
                etLocation?.setText(address)
            }
        }
    }

    companion object {
        private const val MAP_BUTTON_REQUEST_CODE = 57
        const val TAG: String = "CreateServiceFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setTextToolbar(
            getText(R.string.create_new_service).toString(),
            ContextCompat.getColor(requireContext(), R.color.colorWhite)
        )
    }
}

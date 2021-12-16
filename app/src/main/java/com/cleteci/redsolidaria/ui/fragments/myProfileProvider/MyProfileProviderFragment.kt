package com.cleteci.redsolidaria.ui.fragments.myProfileProvider


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Organization
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.fragments.createOrganization.CreateOrganizationFragment
import com.cleteci.redsolidaria.util.LocationResources
import com.cleteci.redsolidaria.util.showAlert
import com.schibstedspain.leku.*
import kotlinx.android.synthetic.main.fragment_my_profile_provider.*
import javax.inject.Inject


class MyProfileProviderFragment : BaseFragment(), MyProfileProviderContract.View {

    var btSend: Button? = null
    var btCancel: Button? = null
    var etName: EditText? = null
    var etLocation: EditText? = null
    var etPhone: EditText? = null
    var etWeb: EditText? = null
    var etDesc: EditText? = null
    var etAboutUs: EditText? = null
    var ivChangeImage: ImageView? = null
    var tvChangePlan: TextView? = null
    var tvPlan: TextView? = null
    var ivImageProfile: ImageView? = null


    @Inject
    lateinit var presenter: MyProfileProviderContract.Presenter
    private lateinit var locationResources: LocationResources

    private lateinit var rootView: View

    fun newInstance(): MyProfileProviderFragment {
        return MyProfileProviderFragment()
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
                startActivityForResult(locationPickerIntent,
                    MAP_BUTTON_REQUEST_CODE
                )
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_my_profile_provider, container, false)

        tvChangePlan = rootView?.findViewById(R.id.tvChangePlan)
        tvPlan = rootView?.findViewById(R.id.tvPlan)

        tvChangePlan?.setOnClickListener {
            Toast.makeText(activity!!, getString(R.string.change_plan_inf), Toast.LENGTH_LONG).show()
        }

        ivChangeImage = rootView?.findViewById(R.id.ivChangeImage)

        ivChangeImage?.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery();
            }

        }

        ivImageProfile = rootView?.findViewById(R.id.ivImageProfile)

        btCancel = rootView?.findViewById(R.id.btCancel)

        btCancel!!.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        etName = rootView?.findViewById<EditText>(R.id.etName);
        etLocation = rootView?.findViewById(R.id.etLocation);
        etWeb = rootView?.findViewById(R.id.etWeb);
        etPhone = rootView?.findViewById(R.id.etPhone);
        etDesc = rootView?.findViewById(R.id.etDesc);
        etAboutUs = rootView?.findViewById(R.id.etAboutUs);

        btSend = rootView?.findViewById(R.id.btSend);

        etLocation!!.setOnClickListener {
            if (locationResources.isLocationEnabled()) {
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

        btSend!!.setOnClickListener {

            if (etName!!.text.isNotEmpty() && etWeb!!.text.isNotEmpty() && etLocation!!.text.isNotEmpty() &&
                etPhone!!.text.isNotEmpty() && etDesc!!.text.isNotEmpty() && etAboutUs!!.text.isNotEmpty()) {
                updateOrganization()
            } else {
                Toast.makeText(context, getString(R.string.missing_data), Toast.LENGTH_LONG).show()
            }
        }

        return rootView
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
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
        presenter.loadData()
    }

    private fun updateOrganization() {
        presenter.updateOrg(etName!!.text.toString(), etWeb!!.text.toString(),
            etPhone!!.text.toString(), etAboutUs!!.text.toString(), etDesc!!.text.toString())
    }

    override fun loadDataSuccess(org: Organization) {
        activity?.runOnUiThread(Runnable {
            etName?.setText(org.name)
            etWeb?.setText(org.webPage)
            etLocation?.setText(org.location)
            etPhone?.setText(org.phone)
            etAboutUs?.setText(org.aboutUs)
            etDesc?.setText(org.servicesDesc)
            tvPlan?.text = org.plan
        })
    }

    override fun savedSuccess() {
        activity?.runOnUiThread(Runnable {
            showDialog()
        })
    }

    private fun showDialog() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.comp_alert_succes_suggest_resource)
        // val body = dialog .findViewById(R.id.body) as TextView

        val yesBtn = dialog.findViewById(R.id.btCont) as Button

        yesBtn.setOnClickListener {
            dialog.dismiss()
            (activity as MainActivity).onBackPressed()
        }

        dialog.show()

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            Log.d("RESULT****", "OK" + requestCode)

            if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
                ivImageProfile?.setImageURI(data?.data)

            }

            if (data != null && requestCode == MAP_BUTTON_REQUEST_CODE) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)

                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)

                val address = data.getStringExtra(LOCATION_ADDRESS)


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

    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
        private val MAP_BUTTON_REQUEST_CODE = 57
        val TAG: String = "MyProfileProviderFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.my_profile).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )

    }


}

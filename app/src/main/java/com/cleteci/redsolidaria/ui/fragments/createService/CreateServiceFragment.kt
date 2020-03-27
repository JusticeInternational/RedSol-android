package com.cleteci.redsolidaria.ui.fragments.createService

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.fragments.suggestService.SuggestServiceFragment
import com.schibstedspain.leku.*
import javax.inject.Inject

class CreateServiceFragment : BaseFragment() , CreateServiceContract.View  {

    var btSend: Button? = null
    var btCancel: Button? = null

    var etName: EditText? = null
    var etLocation: EditText? = null
    var etCategory: EditText? = null
    var etDesc: EditText? = null

    var etSchedule: EditText? = null



    @Inject
    lateinit var presenter: CreateServiceContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): CreateServiceFragment {
        return CreateServiceFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_create_service, container, false)


        btCancel = rootView?.findViewById(R.id.btCancel);

        btCancel!!.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        etName = rootView?.findViewById(R.id.etName);
        etLocation = rootView?.findViewById(R.id.etLocation);
        etCategory = rootView?.findViewById(R.id.etCategory);
        etDesc = rootView?.findViewById(R.id.etDesc);
        etSchedule= rootView?.findViewById(R.id.etSchedule);
        btSend = rootView?.findViewById(R.id.btSend);

        etLocation!!.setOnClickListener {
            val locationPickerIntent = LocationPickerActivity.Builder()
                .withLocation(41.4036299, 2.1743558)
                .withGeolocApiKey(getString(R.string.map_key))
                .withSearchZone("es_ES")
             
                .build(context!!)

            startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE)

        }

        btSend!!.setOnClickListener {

            if (!etName!!.text.isEmpty() && !etLocation!!.text.isEmpty() && !etCategory!!.text.isEmpty() && !etDesc!!.text.isEmpty()){
                showDialog()
            }else{
                Toast.makeText(context, getString(R.string.please_complete_form), Toast.LENGTH_SHORT).show()
            }
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


    private fun showDialog() {
        val dialog = Dialog(activity!!)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        dialog .setCancelable(false)
        dialog .setContentView(R.layout.comp_alert_succes_suggest_resource)
        // val body = dialog .findViewById(R.id.body) as TextView

        val yesBtn = dialog .findViewById(R.id.btCont) as Button

        yesBtn.setOnClickListener {
            dialog .dismiss()
            (activity as MainActivity).onBackPressed()
        }

        dialog .show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RESULT****", "OK" + requestCode)

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

    companion object {
        private val MAP_BUTTON_REQUEST_CODE = 57
        val TAG: String = "CreateServiceFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(getText(R.string.create_new_service).toString(),activity!!.resources.getColor(R.color.colorWhite))

    }



}

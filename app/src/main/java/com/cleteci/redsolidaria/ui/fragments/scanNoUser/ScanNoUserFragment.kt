package com.cleteci.redsolidaria.ui.fragments.infoService

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.*
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.models.ResourceCategory
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import javax.inject.Inject

class ScanNoUserFragment : BaseFragment(), ScanNoUserContract.View {


    var serviceID: String? = null
    var catID: String? = null
    var name: String? = null
    var isGeneric: Boolean = false

    var etName: EditText? = null
    var etLastName: EditText? = null
    var etDNI: EditText? = null
    var etAge: EditText? = null
    var etPhone: EditText? = null
    var etEmail: EditText? = null
    var etAditional: EditText? = null

    var spCountry: Spinner? = null

    var rbM: RadioButton? = null

    var rbF: RadioButton? = null

    var btSend: Button? = null
    var btCancel: Button? = null


    @Inject
    lateinit var presenter: ScanNoUserContract.Presenter

    private lateinit var rootView: View

    fun newInstance(serviceID: String?, catId: String?, name: String?, isGeneric: Boolean): ScanNoUserFragment {
        var frag = ScanNoUserFragment()
        var args = Bundle()
        args.putString("serviceID", serviceID)
        args.putString("catID", catId)
        args.putString("name", name)
        args.putBoolean("isGeneric", isGeneric)
        frag.setArguments(args)

        return frag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            serviceID = arguments!!.getString("serviceID")
            catID = arguments!!.getString("catID")
            name = arguments!!.getString("name")
            isGeneric = arguments!!.getBoolean("isGeneric")

        }
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_scan_no_user, container, false)

        etName = rootView.findViewById(R.id.etName)
        etLastName = rootView.findViewById(R.id.etLastName)
        etDNI = rootView.findViewById(R.id.etDNI)
        etAge = rootView.findViewById(R.id.etAge)
        etPhone = rootView.findViewById(R.id.etPhone)
        etEmail = rootView.findViewById(R.id.etEmail)
        etAditional = rootView.findViewById(R.id.etAditional)

        spCountry = rootView.findViewById(R.id.spCountry)

        rbM = rootView.findViewById(R.id.rbM)

        rbF = rootView.findViewById(R.id.rbF)

        btSend = rootView.findViewById(R.id.btSend)
        btCancel = rootView.findViewById(R.id.btCancel)


        btSend?.setOnClickListener {
            var gender:Int
            if (rbF!=null && rbF!!.isChecked) {
                gender=1
            } else {
                gender=2
            }
            var country:String = spCountry?.getSelectedItem().toString()

            var age:Int?=null
            if (etAge?.text.toString()!=null && !etAge?.text.toString().isEmpty()){
                 age= etAge?.text.toString().toInt()
            }


            if (serviceID != null) {
                presenter.validateAtentionUnregisteredService(
                    BaseApp.prefs.current_org.toString(),
                    serviceID!!, etName?.text.toString(),
                    etLastName?.text.toString(),
                    etDNI?.text.toString(),
                    gender,
               country,
               age,
                etPhone?.text.toString(),
                etEmail?.text.toString(),
                etAditional?.text.toString(),
                name,
                isGeneric)
            } else {

                presenter.validateAtentionUnregisteredCategory(
                    BaseApp.prefs.current_org.toString(),
                    catID!!, etName?.text.toString(),
                    etLastName?.text.toString(),
                    etDNI?.text.toString(),
                    gender,
                    country,
                    age,
                    etPhone?.text.toString(),
                    etEmail?.text.toString(),
                    etAditional?.text.toString(),
                    name
                    )

            }

        }

        btCancel?.setOnClickListener {

            (activity as MainActivity).onBackPressed()

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

    override fun init() {}

    private fun initView() {

    }

    override fun loadDataSuccess(msg: String) {
        activity?.runOnUiThread(Runnable {
            showAlert(R.drawable.ic_check_green, msg)
            (activity as MainActivity).onBackPressed()
        })



    }

    override fun loadDataError(msg: String) {
        activity?.runOnUiThread(Runnable {
            showAlert(R.drawable.ic_error, msg)
        })
    }

    fun showAlert(icon: Int, msg: String) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.comp_alert_succes_suggest_resource)

        val ivIcon = dialog.findViewById(R.id.ivIcon) as ImageView

        val tvAlertMsg = dialog.findViewById(R.id.tvAlertMsg) as TextView

        ivIcon.setImageResource(icon)

        tvAlertMsg.text = msg

        val yesBtn = dialog.findViewById(R.id.btCont) as Button

        yesBtn.setOnClickListener {
            dialog.dismiss()


        }

        dialog.show()
    }


    companion object {
        val TAG: String = "ScanNoUserFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.count_attention).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )

    }
}

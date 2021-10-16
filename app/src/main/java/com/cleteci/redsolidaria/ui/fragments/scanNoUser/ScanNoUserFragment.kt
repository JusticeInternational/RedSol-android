package com.cleteci.redsolidaria.ui.fragments.scanNoUser

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.InputFilter
import android.view.*
import android.widget.*
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.util.*
import com.cleteci.redsolidaria.util.Constants.Companion.DEFAULT_PASSWORD
import com.cleteci.redsolidaria.viewModels.BaseViewModel
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import com.cleteci.redsolidaria.viewModels.UserAccountViewModel
import kotlinx.android.synthetic.main.fragment_scan_no_user.*
import org.koin.android.viewmodel.ext.android.viewModel

class ScanNoUserFragment : BaseFragment() {

    private val organizationVM by viewModel<OrganizationViewModel>()
    private val userAccountVM by viewModel<UserAccountViewModel>()
    private var countriesList: ArrayList<String> = ArrayList()
    private var serviceId: String = ""
    private var categoryId: String = ""
    private var name: String = ""
    private var isGeneric: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also { bundle ->
            serviceId = bundle.getString(SERVICE_ID, "")
            categoryId = bundle.getString(CATEGORY_ID, "")
            name = bundle.getString(NAME, "")
            isGeneric = bundle.getBoolean(IS_GENERIC)
        }

        countriesList = getCountries()
        userAccountVM.status.observe(this, androidx.lifecycle.Observer { status: BaseViewModel.QueryStatus? ->
            when (status) {
                BaseViewModel.QueryStatus.NOTIFY_LOADING -> showLoading(true)
                BaseViewModel.QueryStatus.NOTIFY_SUCCESS -> {
                    showLoading(false)
                    val userName = etName.text.toString() + " " + etLastName.text.toString()
                    showAlert(R.drawable.ic_check_green,
                        getString(R.string.no_user_create_account_success, userName))
                    activity?.onBackPressed()
                }
                BaseViewModel.QueryStatus.NOTIFY_FAILURE -> {
                    showLoading(false)
                    activity?.onBackPressed()
                }
                BaseViewModel.QueryStatus.NOTIFY_UNKNOWN_HOST_FAILURE -> {
                    showLoading(false)
                    activity?.onBackPressed()
                }
                else -> {
                    showLoading(false)
                    activity?.onBackPressed()
                }
            }
        })
        organizationVM.status.observe(this, androidx.lifecycle.Observer { status: BaseViewModel.QueryStatus? ->
            when (status) {
                BaseViewModel.QueryStatus.NOTIFY_LOADING -> showLoading(true)
                BaseViewModel.QueryStatus.NOTIFY_SUCCESS -> {
                    showLoading(false)
                    val userName = etName.text.toString() + " " + etLastName.text.toString()
                    showAlert(R.drawable.ic_check_green,
                        getString(R.string.user_attention_success, userName))
                    userAccountVM.createUser(
                        etName.text.toString(),
                        etLastName.text.toString(),
                        etEmail.text.toString(),
                        DEFAULT_PASSWORD)
                }
                BaseViewModel.QueryStatus.NOTIFY_FAILURE -> {
                    showError(getString(R.string.user_attention_error))
                    showLoading(false)
                    activity?.onBackPressed()
                }
                BaseViewModel.QueryStatus.NOTIFY_UNKNOWN_HOST_FAILURE -> {
                    showInfoDialog(
                        activity,
                        getString(R.string.error_unknown_host_title),
                        getString(R.string.error_unknown_host)
                    )
                    showLoading(false)
                    activity?.onBackPressed()
                }
                else -> {
                    showError(getString(R.string.user_attention_error))
                    showLoading(false)
                    activity?.onBackPressed()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_scan_no_user, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.count_attention).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )

    }

    private fun initView() {
        selectCountry.setOnClickListener {
            val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, countriesList)
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.countries_prompt))
                .setAdapter(adapter) { dialog, which ->
                    selectCountry.text = countriesList[which]
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        etAge.filters = arrayOf<InputFilter>(MinMaxFilter(0, 130))
        btSend.setOnClickListener {
            createUserAttention()
        }

        btCancel?.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }

    private fun createUserAttention() {
        if (isUserDataValid(
            etName.text.toString(),
            etLastName.text.toString(),
            etEmail.text.toString(),
            etPhone.text.toString())) {

            val gender = if (genderFemale.isChecked) 1 else 2
            val country = selectCountry.text.toString()
            val age: Int? = if (etAge.text.toString().isNotEmpty()) etAge.text.toString().toInt() else null

            organizationVM.createUnregisteredUserAttention(
                OrganizationViewModel.CreateAttentionRequest(
                    serviceId,
                    categoryId,
                    etName.text.toString(),
                    etLastName.text.toString(),
                    etIdentification.text.toString(),
                    gender,
                    country,
                    age?: 0,
                    etPhone.text.toString(),
                    etEmail.text.toString(),
                    etAdditionalInformation.text.toString(),
                    name,
                    isGeneric
                ))
        }
    }

    private fun showAlert(icon: Int, msg: String) {
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


    private fun isUserDataValid(name: String?, lastName: String?, email: String?, phone: String?): Boolean {
        if (name == null || name.isEmpty()) {
            Toast.makeText(BaseApp.instance, BaseApp.instance.getString(R.string.invalid_name), Toast.LENGTH_LONG).show()
            return false
        }

        if (lastName == null || lastName.isEmpty()) {
            Toast.makeText(BaseApp.instance, BaseApp.instance.getString(R.string.invalid_lastname), Toast.LENGTH_LONG).show()
            return false
        }

        val trimEmail = email?.trim()
        if (trimEmail == null || trimEmail.isEmpty() || !trimEmail.isValidEmail()) {
            Toast.makeText(BaseApp.instance, BaseApp.instance.getString(R.string.invalid_email), Toast.LENGTH_LONG).show()
            return false
        }

        if (phone != null && phone.isNotEmpty() && !phone.isValidPhone()) {
            Toast.makeText(BaseApp.instance, BaseApp.instance.getString(R.string.invalid_phone), Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    companion object {
        const val TAG: String = "ScanNoUserFragment"
        const val SERVICE_ID = "serviceId"
        const val CATEGORY_ID = "categoryId"
        const val NAME = "name"
        const val IS_GENERIC = "isGeneric"

        fun newInstance(serviceId: String?, categoryId: String?, name: String?, isGeneric: Boolean): ScanNoUserFragment =
            ScanNoUserFragment().apply {
                this.arguments = Bundle().apply {
                    putString(SERVICE_ID, serviceId)
                    putString(CATEGORY_ID, categoryId)
                    putString(NAME, name)
                    putBoolean(IS_GENERIC, isGeneric)
                }
            }
    }
}

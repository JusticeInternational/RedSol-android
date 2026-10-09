package com.cleteci.redsolidaria.ui.fragments.scanNoUser

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.databinding.FragmentScanNoUserBinding
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.util.Constants.Companion.DEFAULT_PASSWORD
import com.cleteci.redsolidaria.util.MinMaxFilter
import com.cleteci.redsolidaria.util.getCountries
import com.cleteci.redsolidaria.util.isValidEmail
import com.cleteci.redsolidaria.util.isValidPhone
import com.cleteci.redsolidaria.util.showInfoDialog
import com.cleteci.redsolidaria.viewModels.BaseViewModel
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import com.cleteci.redsolidaria.viewModels.UserAccountViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ScanNoUserFragment : BaseFragment() {

    private var _binding: FragmentScanNoUserBinding? = null
    private val binding get() = _binding!!
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
            isGeneric = bundle.getBoolean(IS_GENERIC, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanNoUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initView()
    }

    private fun initObservers() {
        countriesList = getCountries()

        userAccountVM.status.observe(viewLifecycleOwner) { status: BaseViewModel.QueryStatus? ->
            when (status) {
                BaseViewModel.QueryStatus.NOTIFY_SUCCESS -> {
                    val userName = binding.etName.text.toString().trim() + " " + binding.etLastName.text.toString().trim()
                    context?.let { ctx ->
                        Toast.makeText(
                            ctx,
                            getString(R.string.no_user_create_account_success, userName),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                BaseViewModel.QueryStatus.EMAIL_ALREADY_REGISTERED -> {
                    context?.let { ctx ->
                        Toast.makeText(
                            ctx,
                            getString(R.string.email_exists),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                BaseViewModel.QueryStatus.NOTIFY_FAILURE -> {
                    Log.w(TAG, "Account creation for unregistered user failed, but attention was recorded")
                }
                else -> {}
            }
        }

        organizationVM.status.observe(viewLifecycleOwner) { status: BaseViewModel.QueryStatus? ->
            when (status) {
                BaseViewModel.QueryStatus.NOTIFY_LOADING -> showLoading(true)
                BaseViewModel.QueryStatus.NOTIFY_SUCCESS -> {
                    showLoading(false)
                    val userName = binding.etName.text.toString().trim() + " " + binding.etLastName.text.toString().trim()
                    showAttentionSuccessDialog(
                        getString(R.string.user_attention_success, userName)
                    )

                    val email = binding.etEmail.text.toString().trim()
                    if (email.isNotEmpty() && email.isValidEmail()) {
                        userAccountVM.createUser(
                            binding.etName.text.toString().trim(),
                            binding.etLastName.text.toString().trim(),
                            email,
                            DEFAULT_PASSWORD
                        )
                    }
                }
                BaseViewModel.QueryStatus.NOTIFY_FAILURE -> {
                    showError(getString(R.string.user_attention_error))
                    showLoading(false)
                }
                BaseViewModel.QueryStatus.NOTIFY_UNKNOWN_HOST_FAILURE -> {
                    showInfoDialog(
                        activity,
                        getString(R.string.error_unknown_host_title),
                        getString(R.string.error_unknown_host)
                    )
                    showLoading(false)
                }
                else -> {
                    showError(getString(R.string.user_attention_error))
                    showLoading(false)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setTextToolbar(
            getText(R.string.count_attention).toString(),
            ContextCompat.getColor(requireContext(), R.color.colorWhite)
        )
    }

    private fun initView() {
        binding.selectCountry.setOnClickListener {
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, countriesList)
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.countries_prompt))
                .setAdapter(adapter) { dialog, which ->
                    binding.selectCountry.text = countriesList[which]
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        binding.etAge.filters = arrayOf<InputFilter>(MinMaxFilter(0, 130))
        binding.btSend.setOnClickListener {
            createUserAttention()
        }

        binding.btCancel.setOnClickListener {
            (activity as? MainActivity)?.onBackPressed()
        }
    }

    private fun createUserAttention() {
        val nameInput = binding.etName.text.toString().trim()
        val lastNameInput = binding.etLastName.text.toString().trim()
        val emailInput = binding.etEmail.text.toString().trim()
        val phoneInput = binding.etPhone.text.toString().trim()
        val countryInput = binding.selectCountry.text.toString().trim()

        if (isUserDataValid(nameInput, lastNameInput, emailInput, phoneInput, countryInput)) {
            val gender = if (binding.genderFemale.isChecked) 1 else 2
            val ageText = binding.etAge.text.toString().trim()
            val age = if (ageText.isNotEmpty()) ageText.toIntOrNull() ?: 0 else 0

            organizationVM.createUnregisteredUserAttention(
                OrganizationViewModel.CreateAttentionRequest(
                    serviceId,
                    categoryId,
                    nameInput,
                    lastNameInput,
                    binding.etIdentification.text.toString().trim(),
                    gender,
                    countryInput,
                    age,
                    phoneInput,
                    emailInput,
                    binding.etAdditionalInformation.text.toString().trim(),
                    name,
                    isGeneric
                )
            )
        }
    }

    private fun showAttentionSuccessDialog(msg: String) {
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

    private fun isUserDataValid(
        name: String,
        lastName: String,
        email: String,
        phone: String,
        country: String
    ): Boolean {
        val ctx = context ?: BaseApp.instance
        if (name.isEmpty()) {
            Toast.makeText(ctx, getString(R.string.invalid_name), Toast.LENGTH_LONG).show()
            return false
        }

        if (lastName.isEmpty()) {
            Toast.makeText(ctx, getString(R.string.invalid_lastname), Toast.LENGTH_LONG).show()
            return false
        }

        val defaultCountryHint = getString(R.string.countries_hint).trim()
        if (country.isEmpty() || country.equals(defaultCountryHint, ignoreCase = true)) {
            Toast.makeText(ctx, getString(R.string.countries_prompt), Toast.LENGTH_LONG).show()
            return false
        }

        if (email.isEmpty() || !email.isValidEmail()) {
            Toast.makeText(ctx, getString(R.string.invalid_email), Toast.LENGTH_LONG).show()
            return false
        }

        if (phone.isNotEmpty() && !phone.isValidPhone()) {
            Toast.makeText(ctx, getString(R.string.invalid_phone), Toast.LENGTH_LONG).show()
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

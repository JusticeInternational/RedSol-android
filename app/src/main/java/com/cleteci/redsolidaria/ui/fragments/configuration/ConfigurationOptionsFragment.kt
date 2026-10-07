package com.cleteci.redsolidaria.ui.fragments.configuration


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import com.cleteci.redsolidaria.BaseApp

import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.data.LocalDataForUITest

import com.cleteci.redsolidaria.databinding.FragmentConfigurationBinding
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.util.SharedPreferences
import com.cleteci.redsolidaria.util.SharedPreferences.Companion.getTestDataPreference
import com.cleteci.redsolidaria.util.SharedPreferences.Companion.putTestDataPreference
import com.cleteci.redsolidaria.util.SharedPreferences.Companion.removeOrganizationSharedPreferences
import javax.inject.Inject


class ConfigurationFragment : BaseFragment(), ConfigurationContract.View {

    private var _binding: FragmentConfigurationBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var presenter: ConfigurationContract.Presenter

    fun newInstance(): ConfigurationFragment {
        return ConfigurationFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root
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
        _binding = null
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
        binding.swTestData.isChecked = getTestDataPreference()
        binding.tvChangePassword.setOnClickListener {
            (activity as MainActivity).showChangePassFragment()
        }
        binding.swTestData.setOnCheckedChangeListener { _, isChecked ->
            putTestDataPreference(isChecked)
            binding.swTestData.isChecked = isChecked
            if (isChecked) {
                val organization = LocalDataForUITest.getOrganizationsList()[0]// Using test data
                BaseApp.sharedPreferences.currentOrganizationId = organization.id
                SharedPreferences.putOrganizationAttributes(organization)
            } else {
                removeOrganizationSharedPreferences()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.config).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )
        if (BaseApp.sharedPreferences.loginLater) {
            showDialog();
        }
    }

    private fun showDialog() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.comp_alert_go_to_login)
        val yesBtn = dialog.findViewById(R.id.btLogin) as Button

        val btCancel = dialog.findViewById(R.id.btCancel) as Button

        yesBtn.setOnClickListener {
            (activity as MainActivity).goToLogin()
            dialog.dismiss()
        }

        btCancel.setOnClickListener {
            activity?.onBackPressed()
            dialog.dismiss()
        }

        dialog.show()
    }

    companion object {
        const val TAG: String = "ChangePassFragment"
    }

}

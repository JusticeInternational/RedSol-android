package com.cleteci.redsolidaria.ui.organizationProfile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.databinding.FragmentOrganizationServicesBinding

class ServicesFragment : Fragment() {

    private var _binding: FragmentOrganizationServicesBinding? = null
    private val binding get() = _binding!!
    private var isHealthSaved = false
    private var isTestSaved = false
    private var isVaccineSaved = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrganizationServicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpUI() {
        if (BaseApp.sharedPreferences.isProviderService) {
            binding.btShowServices.visibility = View.VISIBLE
            binding.btNotifyInterest.visibility = View.GONE
            binding.etHealth.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cross, 0, 0, 0)
            binding.etTest.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_0100_110_covid_tests, 0, 0, 0)
            binding.etVaccine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vaccine, 0, 0, 0)
            binding.btShowServices.setOnClickListener {
                Toast.makeText(context, getString(R.string.in_build), Toast.LENGTH_LONG).show()
            }
        } else {
            binding.btShowServices.visibility = View.GONE
            binding.btNotifyInterest.visibility = View.VISIBLE
            binding.btNotifyInterest.setOnClickListener {
                Toast.makeText(
                    context,
                    "Notificación de interes ha sido enviada al proveedor de los servicios",
                    Toast.LENGTH_LONG
                ).show()
            }
            binding.etHealth.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_cross, 0,
                if (isHealthSaved) R.drawable.ic_interested else R.drawable.ic_interested_green, 0
            )
            binding.etTest.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_0100_110_covid_tests, 0,
                if (isTestSaved) R.drawable.ic_interested else R.drawable.ic_interested_green, 0
            )
            binding.etVaccine.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_vaccine, 0,
                if (isVaccineSaved) R.drawable.ic_interested else R.drawable.ic_interested_green, 0
            )

            binding.etHealth.setOnClickListener {
                val icon =
                    if (isHealthSaved) R.drawable.ic_interested else R.drawable.ic_interested_green
                binding.etHealth.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cross, 0, icon, 0)
                isHealthSaved = !isHealthSaved
            }
            binding.etTest.setOnClickListener {
                val icon =
                    if (isTestSaved) R.drawable.ic_interested else R.drawable.ic_interested_green
                binding.etTest.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_0100_110_covid_tests, 0, icon, 0)
                isTestSaved = !isTestSaved
            }
            binding.etVaccine.setOnClickListener {
                val icon =
                    if (isVaccineSaved) R.drawable.ic_interested else R.drawable.ic_interested_green
                binding.etVaccine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vaccine, 0, icon, 0)
                isVaccineSaved = !isVaccineSaved
            }
        }

    }
}
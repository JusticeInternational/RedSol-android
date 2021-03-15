package com.cleteci.redsolidaria.ui.organization


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cleteci.redsolidaria.R
import kotlinx.android.synthetic.main.activity_organization_profile.*
import kotlinx.android.synthetic.main.fragment_organization_contribute.*
import kotlinx.android.synthetic.main.fragment_organization_services.*


class ServicesFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var isHealthSaved = false
    private var isTestSaved = false
    private var isVaccineSaved = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_organization_services, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpUI()
    }

    private fun setUpUI() {
        etHealth.setOnClickListener {
            val icon = if (isHealthSaved) R.drawable.ic_interested else R.drawable.ic_interested_green
            etHealth.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cross,0, icon, 0)
            isHealthSaved = !isHealthSaved
        }
        etTest.setOnClickListener {
            val icon = if (isTestSaved) R.drawable.ic_interested else R.drawable.ic_interested_green
            etTest.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_test,0, icon, 0)
            isTestSaved = !isTestSaved
        }
        etVaccine.setOnClickListener {
            val icon = if (isVaccineSaved) R.drawable.ic_interested else R.drawable.ic_interested_green
            etVaccine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_vaccine,0, icon, 0)
            isVaccineSaved = !isVaccineSaved
        }
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        @JvmStatic
        fun newInstance(sectionNumber: Int): ServicesFragment {
            return ServicesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}
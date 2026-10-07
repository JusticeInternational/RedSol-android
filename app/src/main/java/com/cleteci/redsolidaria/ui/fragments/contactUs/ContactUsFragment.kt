package com.cleteci.redsolidaria.ui.fragments.contactUs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.databinding.FragmentContactUsBinding
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.util.Constants.Companion.ORGANIZATION_EMAIL
import com.cleteci.redsolidaria.util.Constants.Companion.ORGANIZATION_PHONE
import com.cleteci.redsolidaria.util.openDialerClient
import com.cleteci.redsolidaria.util.openEmailClient

class ContactUsFragment : BaseFragment() {

    private var _binding: FragmentContactUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentContactUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.iconCall.setOnClickListener { openDialerClient(requireContext(), ORGANIZATION_PHONE) }
        binding.iconEmail.setOnClickListener { openEmailClient(requireContext(), ORGANIZATION_EMAIL) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(getString(R.string.contact_us), resources.getColor(R.color.colorWhite))
    }

    companion object {
        const val TAG: String = "ContactUsFragment"
    }

}

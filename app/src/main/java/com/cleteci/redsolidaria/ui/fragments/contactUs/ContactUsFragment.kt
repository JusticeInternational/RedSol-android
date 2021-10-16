package com.cleteci.redsolidaria.ui.fragments.contactUs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.util.Constants.Companion.ORGANIZATION_EMAIL
import com.cleteci.redsolidaria.util.Constants.Companion.ORGANIZATION_PHONE
import com.cleteci.redsolidaria.util.openDialerClient
import com.cleteci.redsolidaria.util.openEmailClient
import kotlinx.android.synthetic.main.fragment_contact_us.*

class ContactUsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_contact_us, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iconCall.setOnClickListener{openDialerClient(requireContext(), ORGANIZATION_PHONE)}
        iconEmail.setOnClickListener{openEmailClient(requireContext(), ORGANIZATION_EMAIL)}
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(getString(R.string.contact_us), resources.getColor(R.color.colorWhite))
    }

    companion object {
        const val TAG: String = "ContactUsFragment"
    }

}

package com.cleteci.redsolidaria.ui.fragments.contactUs


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.util.Constants.Companion.ORGANIZATION_EMAIL
import com.cleteci.redsolidaria.util.Constants.Companion.ORGANIZATION_PHONE
import com.cleteci.redsolidaria.util.openDialerClient
import com.cleteci.redsolidaria.util.openEmailClient
import kotlinx.android.synthetic.main.fragment_contact_us.*
import javax.inject.Inject


class ContactUsFragment : BaseFragment() , ContactUsContract.View  {

    @Inject lateinit var presenter: ContactUsContract.Presenter

    fun newInstance(): ContactUsFragment {
        return ContactUsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_contact_us, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iconCall.setOnClickListener{openDialerClient(context!!, ORGANIZATION_PHONE)}
        iconEmail.setOnClickListener{openEmailClient(context!!, ORGANIZATION_EMAIL)}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
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

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(getString(R.string.contact_us),
            activity!!.resources.getColor(R.color.colorWhite))
    }

    companion object {
        val TAG: String = "ContactUsFragment"
    }

}

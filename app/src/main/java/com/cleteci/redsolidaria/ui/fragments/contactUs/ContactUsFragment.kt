package com.cleteci.redsolidaria.ui.fragments.contactUs


import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.cleteci.redsolidaria.BaseApp

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import javax.inject.Inject


class ContactUsFragment : BaseFragment() , ContactUsContract.View  {

    var btSend: Button? = null

    var etWrite: EditText? = null

    @Inject lateinit var presenter: ContactUsContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): ContactUsFragment {
        return ContactUsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_contact_us, container, false)
        btSend=rootView?.findViewById(R.id.btSend)
        etWrite=rootView?.findViewById(R.id.etWrite)

        btSend?.setOnClickListener{
            if (!etWrite!!.text.isEmpty()){
                showDialogSucces()
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

    companion object {
        val TAG: String = "ContactUsFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(getText(R.string.contact_us).toString(),activity!!.resources.getColor(R.color.colorWhite))
        if (BaseApp.prefs.login_later) {
            showDialog();
        }
    }

    private fun showDialogSucces() {
        val dialog = Dialog(activity!!)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        dialog .setCancelable(false)
        dialog .setContentView(R.layout.comp_alert_succes_suggest_resource)
        val yesBtn = dialog .findViewById(R.id.btCont) as Button

        yesBtn.setOnClickListener {
            dialog .dismiss()
            (activity as MainActivity).onBackPressed()
        }

        dialog .show()

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

}

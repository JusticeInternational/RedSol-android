package com.cleteci.redsolidaria.ui.fragments.changePassword


import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import com.cleteci.redsolidaria.BaseApp

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import javax.inject.Inject
import com.ybs.passwordstrengthmeter.PasswordStrength
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText


class ChangePassFragment : BaseFragment(), ChangePassContract.View, TextWatcher {

    var progressBar:ProgressBar?=null
    var etPass : TextInputEditText?=null
    var btSave: Button? = null

    @Inject lateinit var presenter: ChangePassContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): ChangePassFragment {
        return ChangePassFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_change_pass, container, false)
        progressBar =rootView. findViewById(R.id.progressBar)
        etPass =rootView. findViewById(R.id.etPass)
        etPass?.addTextChangedListener(this);
        btSave = rootView.findViewById(R.id.btSave)
        btSave!!.setOnClickListener {

            presenter.verifyData(etPass!!.text.toString())

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

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        updatePasswordStrengthView(p0.toString());
    }



    private fun injectDependency() {
        val aboutComponent = DaggerFragmentComponent.builder()
                .fragmentModule(FragmentModule())
                .build()

        aboutComponent.inject(this)
    }

    override fun init() {

    }

    override fun errorPass(mdg: String) {
        Toast.makeText(activity, mdg, Toast.LENGTH_SHORT).show()
    }

    override fun saved() {
        activity?.runOnUiThread(Runnable {
            showDialog()
        })
    }

    private fun initView() {
        //presenter.loadMessage()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(getText(R.string.change_pass).toString(),activity!!.resources.getColor(R.color.colorWhite))
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
        dialog.setContentView(R.layout.comp_alert_succes_suggest_resource)
        // val body = dialog .findViewById(R.id.body) as TextView

        val yesBtn = dialog.findViewById(R.id.btCont) as Button

        yesBtn.setOnClickListener {
            dialog.dismiss()
            (activity as MainActivity).onBackPressed()
        }

        dialog.show()
    }

    private fun updatePasswordStrengthView(password: String) {

        if (password.isEmpty()) {

            progressBar?.setProgress(0)
            return
        }

        val str = PasswordStrength.calculateStrength(password)


        progressBar?.getProgressDrawable()!!.setColorFilter(str.color, android.graphics.PorterDuff.Mode.SRC_IN)
        if (str.getText(activity) == "Weak") {
            progressBar?.setProgress(25)
        } else if (str.getText(activity) == "Medium") {
            progressBar?.setProgress(50)
        } else if (str.getText(activity) == "Strong") {
            progressBar?.setProgress(75)
        } else {
            progressBar?.setProgress(100)
        }
    }


    companion object {

        const val TAG: String = "ChangePassFragment"

    }
}

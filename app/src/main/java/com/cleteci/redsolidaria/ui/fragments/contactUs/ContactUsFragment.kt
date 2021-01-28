package com.cleteci.redsolidaria.ui.fragments.contactUs


import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
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
        btSend.setOnClickListener{
            if (etWrite.text.isNotBlank()){
                openEmailClient()
            } else {
                Toast.makeText(context, getString(R.string.please_complete_form), Toast.LENGTH_SHORT).show()
            }
        }
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
        if (BaseApp.prefs.login_later) {
            showDialog();
        }
    }

    private fun openEmailClient() {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "message/rfc822"
        emailIntent.putExtra(Intent.EXTRA_EMAIL,  arrayOf("redsolcontactus@exanple.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject")
        emailIntent.putExtra(Intent.EXTRA_TEXT, etWrite.text)
        try {
            startActivityForResult(Intent.createChooser(emailIntent, getString(R.string.select_email_client)), REQUEST_CODE_EMAIL)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context, "No Email client found!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showDialogSuccess() {
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

    companion object {
        val TAG: String = "ContactUsFragment"
        val REQUEST_CODE_EMAIL = 0
    }

}

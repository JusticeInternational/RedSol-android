package com.cleteci.redsolidaria.ui.fragments.myProfile

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.Button
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_my_profile.*
import javax.inject.Inject


class MyProfileFragment : BaseFragment(), MyProfileContract.View {

    @Inject
    lateinit var presenter: MyProfileContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_my_profile, container, false)

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

    override fun init() {}

    private fun initView() {
        (activity as MainActivity).setTextToolbar(getString(R.string.my_profile), activity!!.resources.getColor(R.color.colorWhite))
        if (BaseApp.prefs.login_later) {
            showDialog()
            ivQR.visibility = View.GONE
            tvQR.visibility = View.GONE
        } else {
            ivQR.visibility = View.VISIBLE
            tvQR.visibility = View.VISIBLE
            val id = "ID " + BaseApp.prefs.user_saved
            tvQR.text = id
        }

        presenter.getQR()
    }

    companion object {
        val TAG: String = "MyProfileFragment"
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
        // val body = dialog .findViewById(R.id.body) as TextView

        val yesBtn = dialog.findViewById(R.id.btLogin) as Button

        val btCancel = dialog.findViewById(R.id.btCancel) as Button

        yesBtn.setOnClickListener {
            (activity as MainActivity).goToLogin()
            //activity?.finish()
            dialog.dismiss()
        }

        btCancel.setOnClickListener {
            activity?.onBackPressed()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun showQR(bitmap: Bitmap) {
        ivQR?.setImageBitmap(bitmap)
    }
}

package com.cleteci.redsolidaria.ui.fragments.myProfile


import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.cleteci.redsolidaria.BaseApp

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import kotlinx.android.synthetic.main.comp_alert_succes_suggest_resource.*
import javax.inject.Inject


class MyProfileFragment : BaseFragment() , MyProfileContract.View  {

    var ivQR: ImageView? = null
    var tvQR: TextView? = null
    @Inject lateinit var presenter: MyProfileContract.Presenter
    private lateinit var rootView: View

    fun newInstance(): MyProfileFragment {
        return MyProfileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_my_profile, container, false)
        ivQR = rootView.findViewById(R.id.ivQR)
        tvQR = rootView.findViewById(R.id.tvQR)
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
        tvQR?.text="ID "+BaseApp.prefs.user_saved.toString()
        presenter.getQR()
    }

    companion object {
        val TAG: String = "MyProfileFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(getText(R.string.my_profile).toString(),activity!!.resources.getColor(R.color.colorWhite))
        if (BaseApp.prefs.login_later) {
            showDialog()
            ivQR?.visibility=View.GONE
            tvQR?.visibility=View.GONE
        } else {
            ivQR?.visibility=View.VISIBLE
            tvQR?.visibility=View.VISIBLE
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

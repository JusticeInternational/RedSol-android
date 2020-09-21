package com.cleteci.redsolidaria.ui.fragments.servicedetail


import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.ResourceCategory

import com.cleteci.redsolidaria.ui.adapters.ResourseCategoryAdapter
import javax.inject.Inject
import android.widget.TextView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.Button
import android.widget.RatingBar
import com.cleteci.redsolidaria.BaseApp
import android.view.MotionEvent
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import android.view.View.OnTouchListener
import com.cleteci.redsolidaria.ui.base.BaseFragment


class ServiceDetailFragment : BaseFragment(), ServiceDetailContract.View, ResourseCategoryAdapter.onItemClickListener {


    var mListRecyclerView: RecyclerView? = null
    var mAdapter: ResourseCategoryAdapter? = null
    private var listCategory = ArrayList<ResourceCategory>()
    var btSave: Button? = null
    var btVolunteer: Button? = null
    var btContribution: Button? = null
    var btDonate: Button? = null
    var btEvaluate: Button? = null
    var btVerify: Button? = null
    var btTake: Button? = null


    @Inject
    lateinit var presenter: ServiceDetailContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): ServiceDetailFragment {
        return ServiceDetailFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_service_detail, container, false)

        mListRecyclerView = rootView?.findViewById(R.id.recyclerView);
        mListRecyclerView?.setLayoutManager(LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


        listCategory=presenter.getData()

        mAdapter = ResourseCategoryAdapter(activity?.applicationContext, listCategory, this, 3)
        mListRecyclerView?.setAdapter(mAdapter);

        val str = SpannableStringBuilder(getString(R.string.be_volunteer))
        str.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            0,
            25,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        var tvBeVoluntary: TextView = rootView?.findViewById(R.id.tvBeVoluntary);

        tvBeVoluntary.text = str

        val str2 = SpannableStringBuilder(getString(R.string.to_contribute))
        str2.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            0,
            20,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        var tvToContribute: TextView = rootView?.findViewById(R.id.tvToContribute);

        tvToContribute.text = str2

        val str3 = SpannableStringBuilder(getString(R.string.to_donate))
        str3.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            0,
            11,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        var tvDonate: TextView = rootView?.findViewById(R.id.tvDonate);

        tvDonate.text = str3


        val str4 = SpannableStringBuilder(getString(R.string.to_tell_experience))
        str4.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            0,
            30,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        var tvTellExperience: TextView = rootView?.findViewById(R.id.tvTellExperience);

        tvTellExperience.text = str4

        val str5 = SpannableStringBuilder(getString(R.string.to_help_to_verify))
        str5.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            0,
            20,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        var tvHelpVerify: TextView = rootView?.findViewById(R.id.tvHelpVerify);

        tvHelpVerify.text = str5


        val str6 = SpannableStringBuilder(getString(R.string.to_get_profile))
        str6.setSpan(
            android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
            0,
            21,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        var tvGetProfile: TextView = rootView?.findViewById(R.id.tvGetProfile)

        tvGetProfile.text = str6

        btSave = rootView?.findViewById(R.id.btSave)
        btVolunteer = rootView?.findViewById(R.id.btVolunteer)
        btContribution = rootView?.findViewById(R.id.btContribution)
        btDonate = rootView?.findViewById(R.id.btDonate)
        btEvaluate = rootView?.findViewById(R.id.btEvaluate)
        btVerify = rootView?.findViewById(R.id.btVerify)
        btTake = rootView?.findViewById(R.id.btTake)


        btSave?.setOnClickListener(){
            showDialogMsg(getString(R.string.resource_saved))

        }

        btVolunteer?.setOnClickListener(){
            showDialogVolunteer()
        }

        btContribution?.setOnClickListener(){
            showDialogMsg(getString(R.string.info_send))
        }

        btDonate?.setOnClickListener(){
            showDialogMsg(getString(R.string.info_send))
        }

        btEvaluate?.setOnClickListener(){
            showDialogEvaluate()
        }

        btVerify?.setOnClickListener(){
            showDialogMsg(getString(R.string.info_send))
        }

        btTake?.setOnClickListener(){
            showDialogMsg(getString(R.string.info_send))
        }



        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
        initView()
        //Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
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


    private fun initView() {
        //presenter.loadMessage()
    }

    override fun init() {

    }

    private fun showDialogLogin() {
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

    private fun showDialogMsg(msg: String) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.comp_alert_succes_suggest_resource)
        // val body = dialog .findViewById(R.id.body) as TextView

        val tvAlertMsg = dialog.findViewById(R.id.tvAlertMsg) as TextView

        tvAlertMsg.setText(msg)

        val yesBtn = dialog.findViewById(R.id.btCont) as Button



        yesBtn.setOnClickListener {

            dialog.dismiss()
        }



        dialog.show()

    }


    private fun showDialogVolunteer() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.comp_alert_be_volunteer)
        // val body = dialog .findViewById(R.id.body) as TextView



        val yesBtn = dialog.findViewById(R.id.btCont) as Button



        yesBtn.setOnClickListener {

            dialog.dismiss()
        }



        dialog.show()

    }

    private fun showDialogEvaluate() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.comp_alert_evaluate)
        // val body = dialog .findViewById(R.id.body) as TextView

        val showRatingBar = dialog.findViewById(R.id.showRatingBar) as RatingBar

        showRatingBar.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_UP) {
                    val touchPositionX = event.x
                    val width = showRatingBar.getWidth()
                    val starsf = touchPositionX / width * 5.0f
                    val stars = starsf.toInt() + 1
                    showRatingBar.setRating(stars.toFloat())

                   // Toast.makeText(this@MainActivity, "test", Toast.LENGTH_SHORT).show()
                    v.isPressed = false
                }
                if (event.action == MotionEvent.ACTION_DOWN) {
                    v.isPressed = true
                }

                if (event.action == MotionEvent.ACTION_CANCEL) {
                    v.isPressed = false
                }




                return true
            }
        })

        val yesBtn = dialog.findViewById(R.id.btCont) as Button



        yesBtn.setOnClickListener {

            Toast.makeText(activity,"Evaluación enviada!", Toast.LENGTH_SHORT).show()

            dialog.dismiss()
        }



        dialog.show()

    }


    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            "Justice International",
            activity!!.resources.getColor(R.color.colorPrimary)
        )

        if (BaseApp.prefs.login_later) {
            showDialogLogin();
        }
    }

    override fun itemDetail(postId: Int) {

    }

    companion object {
        val TAG: String = "ServiceDetailFragment"
    }

}

package com.cleteci.redsolidaria.ui.fragments.myResources


import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleteci.redsolidaria.BaseApp

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourseAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_my_resources.*
import javax.inject.Inject


class MyResoursesFragment : BaseFragment(), MyResourcesContract.View, ResourseAdapter.onItemClickListener {

    @Inject
    private lateinit var presenter: MyResourcesContract.Presenter

    private lateinit var mAdapterPending: ResourseAdapter
    private lateinit var mAdapterSaved: ResourseAdapter
    private lateinit var mAdapterUsed: ResourseAdapter
    private lateinit var mAdapterVolunteering: ResourseAdapter
    private val listPending = ArrayList<Service>()
    private val listSaved = ArrayList<Service>()
    private val listVolunteering = ArrayList<Service>()
    private val listUsed = ArrayList<Service>()

    fun newInstance(): MyResoursesFragment {
        return MyResoursesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View  = inflater.inflate(R.layout.fragment_my_resources, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
        initView()
        presenter.getData()
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
        rvPending.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mAdapterPending = ResourseAdapter(activity?.applicationContext, listPending, this, 2,  false)
        rvPending.adapter = mAdapterPending

        rvSaved.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mAdapterSaved = ResourseAdapter(activity?.applicationContext, listSaved, this, 2,  false)
        rvSaved.adapter = mAdapterSaved

        rvVolunteering.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mAdapterVolunteering = ResourseAdapter(activity?.applicationContext, listVolunteering, this, 2,  false)
        rvVolunteering.adapter = mAdapterVolunteering

        rvUsed.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mAdapterUsed = ResourseAdapter(activity?.applicationContext, listUsed, this, 2,  false)
        rvUsed.adapter = mAdapterUsed
    }

    companion object {
        val TAG: String = "MyResoursesFragment"
    }

    override fun loadDataSuccess(
        pending: List<Service>,
        saved: List<Service>,
        volunteer: List<Service>,
        used: List<Service>
    ) {
        listPending.clear()
        listPending.addAll(pending)
        mAdapterPending?.notifyDataSetChanged()

        listSaved.clear()
        listSaved.addAll(saved)
        mAdapterSaved?.notifyDataSetChanged()

        listUsed.clear()
        listUsed.addAll(used)
        mAdapterUsed?.notifyDataSetChanged()

        listVolunteering.clear()
        listVolunteering.addAll(volunteer)
        mAdapterVolunteering?.notifyDataSetChanged()
    }

    override fun clickDetailResource(postId: Int, name: String, isGeneric:Boolean) {
        (activity as MainActivity).openOrganizationProfile("")
    }

    override fun scanNoUserResource(postId: String, name: String, isGeneric: Boolean) {

    }

    override fun clickScanresourse(postId: String) { }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.my_resources).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )

        if (BaseApp.prefs.login_later) {

            mScrollView?.visibility = View.GONE
            lyEmpty?.visibility = View.VISIBLE


        } else {
            mScrollView?.visibility = View.VISIBLE
            lyEmpty?.visibility = View.GONE


        }
    }


}

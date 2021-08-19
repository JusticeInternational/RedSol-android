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
import com.cleteci.redsolidaria.ui.adapters.ResourceAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_my_resources.*
import javax.inject.Inject


class MyResourcesFragment : BaseFragment(),
        MyResourcesContract.View,
        ResourceAdapter.OnItemClickListener {

    @Inject
    lateinit var presenter: MyResourcesContract.Presenter

    private lateinit var mAdapterPending: ResourceAdapter
    private lateinit var mAdapterSaved: ResourceAdapter
    private lateinit var mAdapterUsed: ResourceAdapter
    private lateinit var mAdapterVolunteering: ResourceAdapter
    private val listPending = ArrayList<Service>()
    private val listSaved = ArrayList<Service>()
    private val listVolunteering = ArrayList<Service>()
    private val listUsed = ArrayList<Service>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_my_resources, container, false)

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

    override fun init() {}

    override fun loadDataSuccess(
        pending: List<Service>,
        saved: List<Service>,
        volunteer: List<Service>,
        used: List<Service>
    ) {
        listPending.clear()
        listPending.addAll(pending)
        mAdapterPending.notifyDataSetChanged()

        listSaved.clear()
        listSaved.addAll(saved)
        mAdapterSaved.notifyDataSetChanged()

        listUsed.clear()
        listUsed.addAll(used)
        mAdapterUsed.notifyDataSetChanged()

        listVolunteering.clear()
        listVolunteering.addAll(volunteer)
        mAdapterVolunteering.notifyDataSetChanged()
    }

    private fun injectDependency() {
        val aboutComponent = DaggerFragmentComponent.builder()
                .fragmentModule(FragmentModule())
                .build()

        aboutComponent.inject(this)
    }

    private fun initView() {
        if (BaseApp.sharedPreferences.loginLater) {
            mScrollView?.visibility = View.GONE
            lyEmpty?.visibility = View.VISIBLE
        } else {
            mScrollView?.visibility = View.VISIBLE
            lyEmpty?.visibility = View.GONE
        }

        (activity as MainActivity).setTextToolbar(
                getString(R.string.my_resources),
                activity!!.resources.getColor(R.color.colorWhite)
        )

        rvPending.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mAdapterPending = ResourceAdapter(activity?.applicationContext, listPending, this, 2,  false)
        rvPending.adapter = mAdapterPending

        rvSaved.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mAdapterSaved = ResourceAdapter(activity?.applicationContext, listSaved, this, 2,  false)
        rvSaved.adapter = mAdapterSaved

        rvVolunteering.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mAdapterVolunteering = ResourceAdapter(activity?.applicationContext, listVolunteering, this, 2,  false)
        rvVolunteering.adapter = mAdapterVolunteering

        rvUsed.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mAdapterUsed = ResourceAdapter(activity?.applicationContext, listUsed, this, 2,  false)
        rvUsed.adapter = mAdapterUsed
    }

    override fun clickDetailResource(postId: Int, name: String, isGeneric:Boolean) {
        (activity as MainActivity).openOrganizationProfile("")
    }

    override fun scanNoUserResource(postId: String, name: String, isGeneric: Boolean) {}

    override fun clickScanResource(postId: String) {}

    companion object {

        const val TAG: String = "MyResourcesFragment"

    }

}

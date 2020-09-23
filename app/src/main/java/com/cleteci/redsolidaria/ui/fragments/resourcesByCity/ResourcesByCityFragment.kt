package com.cleteci.redsolidaria.ui.fragments.resourcesByCity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourseAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.fragments.servicedetail.ServiceDetailFragment
import javax.inject.Inject


class ResourcesByCityFragment : BaseFragment(), ResourcesByCityContract.View,
    ResourseAdapter.onItemClickListener {


    var mListRecyclerView: RecyclerView? = null
    var mAdapter: ResourseAdapter? = null
    private val listCategory = ArrayList<Resource>()

    @Inject
    lateinit var presenter: ResourcesByCityContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): ResourcesByCityFragment {
        return ResourcesByCityFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_resourses_by_city, container, false)

        mListRecyclerView = rootView?.findViewById(R.id.rvResourses);
        mListRecyclerView?.setLayoutManager(LinearLayoutManager(getActivity()));

        // only create and set a new adapter if there isn't already one
        //if (mAdapter == null) {
        mAdapter = ResourseAdapter(activity?.applicationContext, listCategory, this, 1)
        mListRecyclerView?.setAdapter(mAdapter);

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
        presenter.loadData()

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

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).setTextToolbar(
            getText(R.string.search).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )
    }

    override fun loadDataSuccess(list: List<Resource>) {
        activity?.runOnUiThread(Runnable {
            listCategory.clear()
            listCategory.addAll(list)

            mAdapter?.notifyDataSetChanged()
        })
    }

    override fun clickDetailResource(postId: String) {

        activity!!.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container1, ServiceDetailFragment().newInstance(), ServiceDetailFragment.TAG)
            .commit()

    }


    companion object {
        val TAG: String = "RBCFragment"
    }

}

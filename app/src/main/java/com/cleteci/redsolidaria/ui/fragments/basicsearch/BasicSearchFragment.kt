package com.cleteci.redsolidaria.ui.fragments.basicsearch


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.ResourceCategory
import javax.inject.Inject
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourseCategoryAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.fragments.advancedsearch.AdvancedSearchFragment
import com.cleteci.redsolidaria.ui.fragments.resourcesByCity.ResourcesByCityFragment


class BasicSearchFragment : BaseFragment() , BasicSearchContract.View , ResourseCategoryAdapter.onItemClickListener {

    var mListRecyclerView: RecyclerView? = null
    var mAdapter:ResourseCategoryAdapter? = null
    private val listCategory= ArrayList<ResourceCategory>()

    @Inject lateinit var presenter: BasicSearchContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): BasicSearchFragment {
        return BasicSearchFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_basic_search, container, false)

        mListRecyclerView = rootView?.findViewById(R.id.recyclerView);
        mListRecyclerView?.setLayoutManager( GridLayoutManager(getActivity(),2));

        // only create and set a new adapter if there isn't already one
        //if (mAdapter == null) {
        mAdapter = ResourseCategoryAdapter(activity?.applicationContext, listCategory, this, 1)
        mListRecyclerView?.setAdapter(mAdapter);

        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.advancedContainer, AdvancedSearchFragment().newInstance())
            .commit()

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



       override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(getText(R.string.search).toString(),activity!!.resources.getColor(R.color.colorWhite))
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
        presenter.loadData()
    }

    override fun loadDataSuccess(list: List<ResourceCategory>) {
        listCategory.clear()
        listCategory.addAll(list)
        mAdapter?.notifyDataSetChanged()
    }

    override fun itemDetail(postId: Int) {

        activity!!.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container1, ResourcesByCityFragment().newInstance(), ResourcesByCityFragment.TAG)
            .commit()

    }

    companion object {
        val TAG: String = "BasicSearchFragment"
    }

}

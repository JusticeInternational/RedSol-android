package com.cleteci.redsolidaria.ui.fragments.basicsearch


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
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
import com.cleteci.redsolidaria.ui.fragments.resourcesResult.ResourcesResultFragment


class BasicSearchFragment : BaseFragment() , BasicSearchContract.View , ResourseCategoryAdapter.onItemClickListener {


    var mListRecyclerView: RecyclerView? = null
    var mAdapter:ResourseCategoryAdapter? = null
    var searchView: SearchView? = null
    var tvResult: TextView? = null
    private var keyWord: String = ""
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
        mAdapter = ResourseCategoryAdapter(activity?.applicationContext, listCategory, this, 1,  false)
        mListRecyclerView?.adapter = mAdapter;
        searchView = rootView?.findViewById(R.id.searchView);
        searchView!!.isIconified = false

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                setSearchParameter()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                cleanLayout()
                return false
            }
        })

        searchView?.setOnCloseListener {
            searchView?.setQuery("", false)
            false
        }

        searchView?.clearFocus(); // close the keyboard on load

        tvResult = rootView?.findViewById(R.id.tvResult);
        tvResult?.visibility = View.GONE

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

    fun setSearchParameter() {
        val query = searchView?.query.toString()
        if (query != null && query.isNotEmpty()) {
            this.keyWord = query
            tvResult?.visibility = View.VISIBLE
        }
    }

    fun cleanLayout() {
        val query = searchView?.query.toString()
        if (query.isNullOrEmpty()) {
            this.keyWord = ""
            tvResult?.visibility = View.GONE
        } else {
            this.keyWord = query
        }
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
        activity?.runOnUiThread(Runnable {
            listCategory.clear()
            listCategory.addAll(list)
            mAdapter?.notifyDataSetChanged()
        })
    }

    override fun itemDetail(postId: Int) {

        activity!!.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container1, ResourcesResultFragment().newInstance(this.listCategory[postId], keyWord), ResourcesResultFragment.TAG)
            .commit()

    }

    override fun clickScanCategory(postId: String) {


    }

    companion object {
        val TAG: String = "BasicSearchFragment"
    }

}

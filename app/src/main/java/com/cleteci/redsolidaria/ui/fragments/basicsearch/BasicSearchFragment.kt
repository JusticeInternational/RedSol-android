package com.cleteci.redsolidaria.ui.fragments.basicsearch


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.CategoriesAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.fragments.resourcesResult.ResourcesResultFragment
import com.cleteci.redsolidaria.util.showInfoDialog
import com.cleteci.redsolidaria.viewModels.BaseViewModel
import com.cleteci.redsolidaria.viewModels.GeneralViewModel
import kotlinx.android.synthetic.main.fragment_basic_search.*
import org.koin.android.viewmodel.ext.android.viewModel


class BasicSearchFragment : BaseFragment(), CategoriesAdapter.OnItemClickListener {

    private val generalVM by viewModel<GeneralViewModel>()
    var mAdapter: CategoriesAdapter? = null
    private var keyWord: String = ""
    private val listCategory = ArrayList<Category>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        generalVM.usedCategories.observe(this,
            androidx.lifecycle.Observer { usedCategories: ArrayList<Category> ->
                loadDataSuccess(usedCategories)
            })
        generalVM.status.observe(this, androidx.lifecycle.Observer { status: BaseViewModel.QueryStatus? ->
            when (status) {
                BaseViewModel.QueryStatus.NOTIFY_LOADING -> showLoading(true)
                BaseViewModel.QueryStatus.NOTIFY_SUCCESS -> showLoading(false)
                BaseViewModel.QueryStatus.NOTIFY_FAILURE -> {
                    showError(getString(R.string.error_getting_categories))
                    showLoading(false)
                }
                BaseViewModel.QueryStatus.NOTIFY_UNKNOWN_HOST_FAILURE -> {
                    showInfoDialog(
                        activity,
                        getString(R.string.error_unknown_host_title),
                        getString(R.string.error_unknown_host)
                    )
                    showLoading(false)
                }
                else -> {
                    showError(getString(R.string.error_getting_categories))
                    showLoading(false)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_basic_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.search).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )
    }

    fun setSearchParameter() {
        val query = searchView.query.toString()
        if (query.isNotEmpty()) {
            this.keyWord = query
            tvResult?.visibility = View.VISIBLE
        }
    }

    fun cleanLayout() {
        val query = searchView?.query.toString()
        if (query.isEmpty()) {
            this.keyWord = ""
            tvResult?.visibility = View.GONE
        } else {
            this.keyWord = query
        }
    }

    private fun initView() {
        recyclerView.layoutManager = GridLayoutManager(activity, 2);

        mAdapter = CategoriesAdapter(activity?.applicationContext, listCategory, this, 1, false)
        recyclerView.adapter = mAdapter
        searchView.isIconified = false

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                setSearchParameter()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                cleanLayout()
                return false
            }
        })

        searchView.setOnCloseListener {
            searchView?.setQuery("", false)
            false
        }

        searchView.clearFocus() // close the keyboard on load
        tvResult.visibility = View.GONE

        generalVM.getUsedCategories()
    }

    fun loadDataSuccess(list: List<Category>) {
        activity?.runOnUiThread {
            listCategory.clear()
            listCategory.addAll(list)
            mAdapter?.notifyDataSetChanged()
        }
    }

    override fun itemDetail(postId: Int) {
        activity!!.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.container_fragment,
                ResourcesResultFragment.newInstance(this.listCategory[postId], keyWord),
                ResourcesResultFragment.TAG
            )
            .commit()

    }

    override fun scanNoUserCategory(position: Int) {}

    override fun clickScanCategory(position: Int) {}

    companion object {

        const val TAG: String = "BasicSearchFragment"

    }

}

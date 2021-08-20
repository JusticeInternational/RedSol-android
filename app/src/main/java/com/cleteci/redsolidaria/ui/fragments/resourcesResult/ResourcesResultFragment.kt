package com.cleteci.redsolidaria.ui.fragments.resourcesResult


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.search.OrganizationsCategorySearchAdapter
import com.cleteci.redsolidaria.ui.search.OrganizationsCategorySearchAdapter.OrganizationCategory
import com.cleteci.redsolidaria.util.showInfoDialog
import com.cleteci.redsolidaria.viewModels.BaseViewModel
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import kotlinx.android.synthetic.main.fragment_resourses_result.*
import org.koin.android.viewmodel.ext.android.viewModel


class ResourcesResultFragment : BaseFragment(),
    OrganizationsCategorySearchAdapter.OnItemClickListener {

    private val organizationVM by viewModel<OrganizationViewModel>()
    private lateinit var mAdapter: OrganizationsCategorySearchAdapter
    private lateinit var categoryId: String
    private lateinit var categoryName: String
    private lateinit var keyWord: String
    private var categoryIconId: Int = 0
    private val organizationsCategoryList = ArrayList<OrganizationCategory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also { bundle ->
            categoryId = bundle.getString(CATEGORY_ID, "")
            categoryName = bundle.getString(CATEGORY_NAME_ID, "")
            categoryIconId = bundle.getInt(ICON_ID, 0)
            keyWord = bundle.getString(KEY_WORD_ID, "")
        }

        organizationVM.organizationsCategoryList.observe(this,
            androidx.lifecycle.Observer { services: ArrayList<OrganizationCategory> ->
                loadDataSuccess(services)
            })
        organizationVM.status.observe(this, androidx.lifecycle.Observer { status: BaseViewModel.QueryStatus? ->
            when (status) {
                BaseViewModel.QueryStatus.NOTIFY_LOADING -> showLoading(true)
                BaseViewModel.QueryStatus.NOTIFY_SUCCESS -> showLoading(false)
                BaseViewModel.QueryStatus.NOTIFY_FAILURE -> {
                    showError(getString(R.string.error_getting_organization))
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
                    showError(getString(R.string.error_getting_organization))
                    showLoading(false)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  = inflater.inflate(R.layout.fragment_resourses_result, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        imageView.setImageResource(categoryIconId)
        rvCategories.layoutManager = LinearLayoutManager(activity)
        mAdapter = OrganizationsCategorySearchAdapter(requireContext(), organizationsCategoryList, this)
        rvCategories.adapter = mAdapter;
        tvResult?.visibility = View.GONE
        rvCategories.visibility = View.GONE
        organizationVM.getOrganizationsByCategory(categoryId, keyWord, categoryIconId, categoryName)
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).setTextToolbar(
            getText(R.string.search).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )
    }

    fun loadDataSuccess(list: List<OrganizationCategory>) {
        activity?.runOnUiThread {
            organizationsCategoryList.clear()
            organizationsCategoryList.addAll(list)
            if(organizationsCategoryList.isEmpty()) {
                tvResult?.visibility = View.VISIBLE
            } else {
                rvCategories.visibility = View.VISIBLE
            }

            mAdapter.notifyDataSetChanged()
        }
    }


    override fun onOrganizationCategorySearchClicked(position: Int) {
        (activity as MainActivity).openOrganizationProfile(organizationsCategoryList[position].organizationId)
    }

    companion object {

        const val TAG = "ResourcesResultFragment"
        private const val CATEGORY_ID = "category_id"
        private const val CATEGORY_NAME_ID = "category_name_id"
        private const val ICON_ID = "icon_id"
        private const val KEY_WORD_ID = "key_work_id"

        fun newInstance(category: Category, keyWord: String = ""): ResourcesResultFragment =
            ResourcesResultFragment().apply {
                this.arguments =  Bundle().apply {
                    putString(CATEGORY_ID, category.id)
                    putString(CATEGORY_NAME_ID, category.name)
                    putInt(ICON_ID, category.iconId)
                    putString(KEY_WORD_ID, keyWord)
                }
        }
    }

}

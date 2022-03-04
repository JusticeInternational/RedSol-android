package com.cleteci.redsolidaria.ui.resources

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.models.Organization
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourcesAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.base.withArguments
import com.cleteci.redsolidaria.util.showInfoDialog
import com.cleteci.redsolidaria.viewModels.BaseViewModel
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import kotlinx.android.synthetic.main.fragment_resources_offered.*
import org.koin.android.viewmodel.ext.android.viewModel


class ResourcesOfferedFragment : BaseFragment(), ResourcesAdapter.OnResourceClickListener {

    private val organizationVM by viewModel<OrganizationViewModel>()
    private lateinit var mServicesAdapter: ResourcesAdapter
    private lateinit var mCategoriesAdapter: ResourcesAdapter
    private val listCategories = ArrayList<Resource>()
    private val listServices = ArrayList<Resource>()
    private var isFromScan = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isFromScan = arguments?.getBoolean(IS_FROM_SCAN) ?: false

        injectDependency()

        mCategoriesAdapter = ResourcesAdapter(requireContext(), listCategories, this, isFromScan)
        mServicesAdapter = ResourcesAdapter(requireContext(), listServices, this, isFromScan)

        organizationVM.organizationLists.observe(this,
            androidx.lifecycle.Observer { organizationLists: Organization.OrganizationLists ->
                loadDataSuccess(organizationLists.offeredCategories,
                    organizationLists.offeredServices
                )
            })
        organizationVM.status.observe(this, androidx.lifecycle.Observer { status: BaseViewModel.QueryStatus? ->
            when (status) {
                BaseViewModel.QueryStatus.NOTIFY_LOADING -> showLoading(true)
                BaseViewModel.QueryStatus.NOTIFY_SUCCESS -> {
                    showLoading(false)
                }
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
    ): View = inflater.inflate(R.layout.fragment_resources_offered, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val organizationId = BaseApp.sharedPreferences.currentOrganizationId
        val userId = BaseApp.sharedPreferences.userSaved
        if (!userId.isNullOrEmpty() && !organizationId.isNullOrEmpty()) {
            lyEmpty.visibility = GONE
            scrollView.visibility = VISIBLE
            organizationVM.getServicesAndCategories(userId)
        } else {
            lyEmpty.visibility = VISIBLE
            scrollView.visibility = GONE
        }

        initView()
    }

    fun loadDataSuccess(categories: List<Category>, services: List<Service>) {
        if (categories.isNullOrEmpty() && services.isNullOrEmpty()) {
            lyEmpty.visibility = VISIBLE
            scrollView.visibility = GONE
        } else {
            lyEmpty.visibility = GONE
            scrollView.visibility = VISIBLE

            activity?.runOnUiThread {
                listCategories.clear()
                categories.forEach {
                    listCategories.add(Resource(category = it))
                }
                mCategoriesAdapter.notifyDataSetChanged()

                listServices.clear()
                services.forEach {
                    listServices.add(Resource(service = it))
                }
                mServicesAdapter.notifyDataSetChanged()

                setupLabels()
            }
        }
    }

    private fun initView() {
        val activity = activity as MainActivity
        if (isFromScan) {
            activity.setTextToolbar(
                getText(R.string.count_resources).toString(),
                activity.resources.getColor(R.color.colorWhite)
            )
            fab.visibility = GONE
        } else {
            activity.setTextToolbar(
                getText(R.string.my_resources).toString(),
                activity.resources.getColor(R.color.colorWhite)
            )
            fab.setOnClickListener {
                activity.openCreateServiceFragment()
            }
            fab.visibility = VISIBLE
        }

        rvMyCategories.layoutManager = LinearLayoutManager(activity)
        rvCategories.layoutManager = LinearLayoutManager(activity)

        rvMyCategories.adapter = mCategoriesAdapter
        rvCategories.adapter = mServicesAdapter
    }

    private fun injectDependency() {
        val aboutComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        aboutComponent.inject(this)
    }

    private fun setupLabels() {
        tvLabelResources?.visibility = if (listServices.isEmpty()) GONE else VISIBLE
        tvLabelCategories?.visibility = if (listCategories.isEmpty()) GONE else VISIBLE
    }

    override fun resourceDetail(position: Int, type: Resource.Type) {
        val activity = activity as MainActivity
        when (type) {
            Resource.Type.CATEGORY ->
                activity.openInfoFragment(Resource(category = listCategories[position].category!!))
            Resource.Type.SERVICE ->
                activity.openInfoFragment(Resource(service = listServices[position].service!!))
        }
    }

    override fun resourceScan(position: Int, type: Resource.Type) {
        val activity = activity as MainActivity
        when (type) {
            Resource.Type.CATEGORY -> {
                val category = listCategories[position].category!!
                activity.showScanFragment(null, category.id, category.name, true)
            }
            Resource.Type.SERVICE -> {
                val service = listServices[position].service!!
                activity.showScanFragment(service.id, null, service.name, service.isGeneric)
            }
        }
    }

    override fun resourceScanNoUser(position: Int, type: Resource.Type) {
        val activity = activity as MainActivity
        when (type) {
            Resource.Type.CATEGORY -> {
                val category = listCategories[position].category!!
                activity.openScanNoUserFragment(null, category.id, category.name, false)
            }
            Resource.Type.SERVICE -> {
                val service = listServices[position].service!!
                activity.openScanNoUserFragment(service.id, null, service.name, service.isGeneric)
            }
        }
    }

    override fun resourceOrganizationProfile(resource: Resource, type: Resource.Type) {

    }

    companion object {
        const val TAG: String = "ResourcesProviderFragment"
        const val IS_FROM_SCAN: String = "isFromScan"

        fun newInstance(isFromScan: Boolean): ResourcesOfferedFragment {
            return ResourcesOfferedFragment().withArguments(1) {
                putBoolean(IS_FROM_SCAN, isFromScan)
            }
        }
    }

}

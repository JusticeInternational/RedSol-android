package com.cleteci.redsolidaria.ui.resources

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.databinding.FragmentResourcesOfferedBinding
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.models.Organization
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourcesAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.base.withArguments
import com.cleteci.redsolidaria.util.showInfoDialog
import com.cleteci.redsolidaria.viewModels.BaseViewModel
import com.cleteci.redsolidaria.viewModels.OrganizationViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ResourcesOfferedFragment : BaseFragment(), ResourcesAdapter.OnResourceClickListener {

    private var _binding: FragmentResourcesOfferedBinding? = null
    private val binding get() = _binding!!
    private val organizationVM by viewModel<OrganizationViewModel>()
    private lateinit var mServicesAdapter: ResourcesAdapter
    private lateinit var mCategoriesAdapter: ResourcesAdapter
    private val listCategories = ArrayList<Resource>()
    private val listServices = ArrayList<Resource>()
    private var isFromScan = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isFromScan = arguments?.getBoolean(IS_FROM_SCAN) ?: false

        mCategoriesAdapter = ResourcesAdapter(requireContext(), listCategories, this, isFromScan)
        mServicesAdapter = ResourcesAdapter(requireContext(), listServices, this, isFromScan)

        organizationVM.organizationLists.observe(this) { organizationLists: Organization.OrganizationLists ->
            loadDataSuccess(
                organizationLists.offeredCategories,
                organizationLists.offeredServices
            )
        }
        organizationVM.status.observe(this) { status: BaseViewModel.QueryStatus? ->
            if (_binding != null) {
                binding.swipeRefresh.isRefreshing = false
            }
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
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResourcesOfferedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        fetchData()
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as? MainActivity
        if (isFromScan) {
            activity?.setTextToolbar(
                getText(R.string.count_resources).toString(),
                ContextCompat.getColor(requireContext(), R.color.colorWhite)
            )
        } else {
            activity?.setTextToolbar(
                getText(R.string.my_resources).toString(),
                ContextCompat.getColor(requireContext(), R.color.colorWhite)
            )
        }
        fetchData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchData() {
        val organizationId = BaseApp.sharedPreferences.currentOrganizationId
        val userId = BaseApp.sharedPreferences.userSaved
        if (!userId.isNullOrEmpty() && !organizationId.isNullOrEmpty()) {
            organizationVM.getServicesAndCategories(userId)
        } else {
            if (_binding != null) {
                binding.swipeRefresh.isRefreshing = false
                binding.lyEmpty.visibility = VISIBLE
                binding.scrollView.visibility = GONE
            }
        }
    }

    fun loadDataSuccess(categories: List<Category>, services: List<Service>) {
        if (_binding == null) return
        binding.swipeRefresh.isRefreshing = false

        if (categories.isNullOrEmpty() && services.isNullOrEmpty()) {
            binding.lyEmpty.visibility = VISIBLE
            binding.scrollView.visibility = GONE
        } else {
            binding.lyEmpty.visibility = GONE
            binding.scrollView.visibility = VISIBLE

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
        val activity = activity as? MainActivity
        if (isFromScan) {
            binding.fab.visibility = GONE
        } else {
            binding.fab.setOnClickListener {
                activity?.openCreateServiceFragment()
            }
            binding.fab.visibility = VISIBLE
        }

        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        binding.swipeRefresh.setOnRefreshListener {
            fetchData()
        }

        binding.rvMyCategories.layoutManager = LinearLayoutManager(activity)
        binding.rvCategories.layoutManager = LinearLayoutManager(activity)

        binding.rvMyCategories.adapter = mCategoriesAdapter
        binding.rvCategories.adapter = mServicesAdapter
    }

    private fun setupLabels() {
        if (_binding == null) return
        binding.tvLabelResources.visibility = if (listServices.isEmpty()) GONE else VISIBLE
        binding.tvLabelCategories.visibility = if (listCategories.isEmpty()) GONE else VISIBLE
    }

    override fun resourceDetail(position: Int, type: Resource.Type) {
        val activity = activity as MainActivity
        when (type) {
            Resource.Type.CATEGORY ->
                activity.openInfoFragment(Resource(category = listCategories[position].category!!))
            Resource.Type.SERVICE ->
                activity.openInfoFragment(Resource(service = listServices[position].service!!))
            else -> {}
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
            else -> {}
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
            else -> {}
        }
    }

    override fun resourceOrganizationProfile(resource: Resource, type: Resource.Type) {}

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

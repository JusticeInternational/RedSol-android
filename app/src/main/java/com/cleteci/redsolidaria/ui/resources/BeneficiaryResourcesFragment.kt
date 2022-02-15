package com.cleteci.redsolidaria.ui.resources

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.GetServicesQuery
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Beneficiary
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourcesAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.util.showInfoDialog
import com.cleteci.redsolidaria.viewModels.BaseViewModel
import com.cleteci.redsolidaria.viewModels.BeneficiaryViewModel
import com.cleteci.redsolidaria.viewModels.GeneralViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_my_resources.*
import kotlinx.android.synthetic.main.fragment_my_resources.lyEmpty
import org.koin.android.viewmodel.ext.android.viewModel


class BeneficiaryResourcesFragment : BaseFragment(), ResourcesAdapter.OnResourceClickListener {

    private val beneficiaryVM by viewModel<BeneficiaryViewModel>()
    private lateinit var mAdapterPending: ResourcesAdapter
    private lateinit var mAdapterSaved: ResourcesAdapter
    private lateinit var mAdapterUsed: ResourcesAdapter
    private lateinit var mAdapterVolunteering: ResourcesAdapter
    private val listPending = ArrayList<Resource>()
    private val listSaved = ArrayList<Resource>()
    private val listVolunteering = ArrayList<Resource>()
    private val listUsed = ArrayList<Resource>()
    private val generalVM by viewModel<GeneralViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         generalVM.services.observe(this,
             androidx.lifecycle.Observer { services: List<GetServicesQuery.Service> ->
                 loadDataSuccess(
                     services,
                     services,
                     services,
                     services
                 )
            })
     //    beneficiaryVM.resourcesLists.observe(this,
     //        androidx.lifecycle.Observer { resourcesLists: Beneficiary.ResourcesLists ->
     //            Log.d("TAG77", "message "+resourcesLists)
//
     //            loadDataSuccess2(
     //                resourcesLists.pending,
     //                resourcesLists.saved,
     //                resourcesLists.volunteering,
     //                resourcesLists.used
     //            )
     //        })
        beneficiaryVM.status.observe(
            this,
            androidx.lifecycle.Observer { status: BaseViewModel.QueryStatus? ->
                when (status) {
                    BaseViewModel.QueryStatus.NOTIFY_LOADING -> showLoading(true)
                    BaseViewModel.QueryStatus.NOTIFY_SUCCESS -> {
                        showLoading(false)
                    }
                    BaseViewModel.QueryStatus.NOTIFY_FAILURE -> {
                        showError(getString(R.string.error_getting_user_resources))
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
                        showError(getString(R.string.error_getting_user_resources))
                        showLoading(false)
                    }
                }
            })
        generalVM.getServices()
    }
    private fun hideCreateOrganization() {
        val item: MenuItem = navView.menu.findItem(R.id.nav_create_organization)
        if (item != null) item.isVisible = false
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_my_resources, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    override fun onResume() {
        super.onResume()
        Log.d("TAG66", "ACA    ACAA")

        // put your code here...
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

        rvPending.layoutManager = LinearLayoutManager(requireContext())
        mAdapterPending = ResourcesAdapter(requireContext(), listPending, this, false)
        rvPending.adapter = mAdapterPending

        rvSaved.layoutManager = LinearLayoutManager(requireContext())
        mAdapterSaved = ResourcesAdapter(requireContext(), listSaved, this, false)
        rvSaved.adapter = mAdapterSaved

        rvUsed.layoutManager = LinearLayoutManager(requireContext())
        mAdapterUsed = ResourcesAdapter(requireContext(), listUsed, this, false)
        rvUsed.adapter = mAdapterUsed

        rvVolunteering.layoutManager = LinearLayoutManager(requireContext())
        mAdapterVolunteering = ResourcesAdapter(requireContext(), listVolunteering, this, false)
        rvVolunteering.adapter = mAdapterVolunteering

        beneficiaryVM.getResources()
    }
    fun loadDataSuccess2(
        pending: List<Service>,
        saved: List<Service>,
        volunteer: List<Service>,
        used: List<Service>
    ) {


        listPending.clear()
        pending.forEach {
            listPending.add(Resource(service = it))
        }
        mAdapterSaved.notifyDataSetChanged()

        listSaved.clear()
        saved.forEach {
              listSaved.add(Resource(service = it))
        }
        mAdapterSaved.notifyDataSetChanged()

        listUsed.clear()
        used.forEach {
              listUsed.add(Resource(service = it))
        }
        mAdapterUsed.notifyDataSetChanged()

        listVolunteering.clear()
        volunteer.forEach {
              listVolunteering.add(Resource(service = it))
        }
        mAdapterVolunteering.notifyDataSetChanged()

        setupLabels()
    }
    fun loadDataSuccess(
        pending: List<GetServicesQuery.Service>,
        saved: List<GetServicesQuery.Service>,
        volunteer: List<GetServicesQuery.Service>,
        used: List<GetServicesQuery.Service>
    ) {

        listPending.clear()
        pending.forEach {
            val service =
                Service(
                    it.id(), it.name(), null, "", "", "", "",
                    it.description(), false, it.serviceCategory()
                )
            listPending.add(Resource(service = service))
        }
        mAdapterPending.notifyDataSetChanged()

        listSaved.clear()
        saved.forEach {
          //  listSaved.add(Resource(service = it))
        }
        mAdapterSaved.notifyDataSetChanged()

        listUsed.clear()
        used.forEach {
          //  listUsed.add(Resource(service = it))
        }
        mAdapterUsed.notifyDataSetChanged()

        listVolunteering.clear()
        volunteer.forEach {
         //   listVolunteering.add(Resource(service = it))
        }
        mAdapterVolunteering.notifyDataSetChanged()

        setupLabels()
    }

    private fun setupLabels() {
        tvPending?.visibility = if (listPending.isEmpty()) View.GONE else View.VISIBLE
        tvSaved?.visibility = if (listSaved.isEmpty()) View.GONE else View.VISIBLE
        tvUsed?.visibility = if (listUsed.isEmpty()) View.GONE else View.VISIBLE
        tvVolunteering?.visibility = if (listVolunteering.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun resourceDetail(position: Int, type: Resource.Type) {
        (activity as MainActivity).openOrganizationProfile("")
    }

    override fun resourceScan(position: Int, type: Resource.Type) {
    }

    override fun resourceScanNoUser(position: Int, type: Resource.Type) {
    }

    companion object {

        const val TAG: String = "MyResourcesFragment"

    }

}

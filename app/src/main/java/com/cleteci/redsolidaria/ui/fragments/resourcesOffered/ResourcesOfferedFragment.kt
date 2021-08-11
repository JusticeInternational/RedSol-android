package com.cleteci.redsolidaria.ui.fragments.resourcesOffered

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourceAdapter
import com.cleteci.redsolidaria.ui.adapters.CategoriesAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.base.withArguments
import kotlinx.android.synthetic.main.fragment_resources_offered.*
import javax.inject.Inject


class ResourcesOfferedFragment :
        BaseFragment(),
        ResourcesOfferedContract.View,
        CategoriesAdapter.OnItemClickListener,
        ResourceAdapter.OnItemClickListener {

    @Inject
    lateinit var presenter: ResourcesOfferedContract.Presenter

    private lateinit var genericResourcesAdapter: ResourceAdapter
    private lateinit var resourcesAdapter: ResourceAdapter
    private lateinit var mAdapter: CategoriesAdapter
    private val listResources = ArrayList<Service>()
    private val listCategories = ArrayList<Category>()
    private val listGenericResources = ArrayList<Service>()
    private var isFromScan = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isFromScan = arguments?.getBoolean(IS_FROM_SCAN) ?: false

        injectDependency()

        mAdapter = CategoriesAdapter(context, listCategories, this, 4, isFromScan)
        resourcesAdapter = ResourceAdapter(context, listResources, this, 3, isFromScan)
        genericResourcesAdapter = ResourceAdapter(context, listGenericResources, this, 3, isFromScan)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_resources_offered, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
        presenter.getData()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }

    override fun init() {

    }

    override fun loadDataSuccess(
            pending: List<Category>,
            services: List<Service>,
            genericServices: List<Service>
    ) {
        activity?.runOnUiThread {
            listCategories.clear()
            listCategories.addAll(pending)
            mAdapter.notifyDataSetChanged()

            listResources.clear()
            listResources.addAll(services)
            resourcesAdapter.notifyDataSetChanged()

            setupLabels()

            listGenericResources.clear()
            listGenericResources.addAll(genericServices)
            genericResourcesAdapter.notifyDataSetChanged()
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

        setupLabels()

        rvMyCategories.layoutManager = LinearLayoutManager(activity)
        rvCategories.layoutManager = LinearLayoutManager(activity)
        rvResourcesGeneric.layoutManager = LinearLayoutManager(activity)

        rvMyCategories.adapter = mAdapter
        rvCategories.adapter = resourcesAdapter
        rvResourcesGeneric.adapter = genericResourcesAdapter
    }

    private fun injectDependency() {
        val aboutComponent = DaggerFragmentComponent.builder()
                .fragmentModule(FragmentModule())
                .build()

        aboutComponent.inject(this)
    }

    private fun setupLabels() {
        tvLabelResources?.visibility = if (listResources.isEmpty()) GONE else VISIBLE
        tvLabelCategories?.visibility = if (listCategories.isEmpty()) GONE else VISIBLE
    }

    override fun itemDetail(postId: Int) {
        val mainActivity = activity as MainActivity
        if (isFromScan) {
            mainActivity.showScanFragment(null, listCategories[postId].id, listCategories[postId].name, true)
        } else {
            mainActivity.openInfoFragment(listCategories[postId], null)
        }
    }

    override fun clickDetailResource(postId: Int, name: String, isGeneric: Boolean) {
        val activity = activity as MainActivity
        if (isFromScan) {
            activity.showScanFragment(listResources[postId].id, null, name, isGeneric)
        } else {
            if (isGeneric) {
                activity.openInfoFragment(null, listGenericResources[postId])
            } else {
                activity.openInfoFragment(null, listResources[postId])
            }
        }
    }

    override fun scanNoUserCategory(position: Int) {
        (activity as MainActivity).openScanNoUserFragment(null, listCategories[position].id, listCategories[position].name, false)
    }

    override fun scanNoUserResource(postId: String, name: String, isGeneric: Boolean) {
        (activity as MainActivity).openScanNoUserFragment(postId, null, name, isGeneric)

    }

    override fun clickScanResource(postId: String) {}

    override fun clickScanCategory(position: Int) {}


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

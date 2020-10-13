package com.cleteci.redsolidaria.ui.fragments.resoursesOffered


import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.models.ResourceCategory
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourseAdapter
import com.cleteci.redsolidaria.ui.adapters.ResourseCategoryAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject
import com.google.gson.Gson


class ResoursesOfferedFragment : BaseFragment(), ResoursesOfferedContract.View,
    ResourseCategoryAdapter.onItemClickListener, ResourseAdapter.onItemClickListener {


    var isFromScan = false

    var tvLabelResourses: TextView? = null
    var tvLabelCategories: TextView? = null

    var rvResourses: RecyclerView? = null
    var rvResoursesGeneric: RecyclerView? = null
    var rvMyCategories: RecyclerView? = null
    var mAdapter: ResourseCategoryAdapter? = null
    var resoursesAdapter: ResourseAdapter? = null
    var genericResoursesAdapter: ResourseAdapter? = null

    var fab: FloatingActionButton? = null
    private val listCategories = ArrayList<ResourceCategory>()
    private val listResourses = ArrayList<Resource>()
    private val listGenericResourses = ArrayList<Resource>()


    @Inject
    lateinit var presenter: ResoursesOfferedContract.Presenter

    private lateinit var rootView: View

    fun newInstance(isFromScan: Boolean): ResoursesOfferedFragment {
        val fragment = ResoursesOfferedFragment()
        val args = Bundle()
        args.putBoolean("isScan", isFromScan)
        fragment.setArguments(args)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isFromScan = arguments!!.getBoolean("isScan")

        }
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_resourses_offered, container, false)

        rvMyCategories = rootView.findViewById(R.id.rvMyCategories)
        rvResourses = rootView.findViewById(R.id.rvResourses)
        rvResoursesGeneric = rootView.findViewById(R.id.rvResoursesGeneric)
        tvLabelResourses = rootView.findViewById(R.id.tvLabelResourses)
        tvLabelCategories = rootView.findViewById(R.id.tvLabelCategories)
        showLabels()

        fab = rootView.findViewById(R.id.fab)
        if (!isFromScan){
        fab?.setOnClickListener {
            (activity as MainActivity).openCreateServiceFragment()
        }
            fab?.visibility= VISIBLE

        }else{
            fab?.visibility= GONE
        }
        rvMyCategories?.setLayoutManager(
            LinearLayoutManager(getActivity())
        )
        rvResourses?.setLayoutManager(
            LinearLayoutManager(getActivity())
        )
        rvResoursesGeneric?.setLayoutManager(
            LinearLayoutManager(getActivity())
        )
        mAdapter = ResourseCategoryAdapter(
            activity?.applicationContext, listCategories, this, 4, isFromScan
        )

        resoursesAdapter = ResourseAdapter(
            activity?.applicationContext, listResourses, this, 3, isFromScan
        )

        genericResoursesAdapter = ResourseAdapter(
            activity?.applicationContext, listGenericResourses, this, 3, isFromScan
        )



        rvMyCategories?.setAdapter(mAdapter)
        rvResourses?.setAdapter(resoursesAdapter)
        rvResoursesGeneric?.setAdapter(genericResoursesAdapter)
        return rootView
    }

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

    override fun loadDataSuccess(
        pending: List<ResourceCategory>,
        services: List<Resource>,
        genericServices: List<Resource>
    ) {
        activity?.runOnUiThread(Runnable {
            listCategories.clear()
            listCategories.addAll(pending)
            mAdapter?.notifyDataSetChanged()

            listResourses.clear()
            listResourses.addAll(services)
            resoursesAdapter?.notifyDataSetChanged()


            showLabels()

            listGenericResourses.clear()
            listGenericResourses.addAll(genericServices)
            genericResoursesAdapter?.notifyDataSetChanged()
        })
    }

    fun showLabels() {
        if (listResourses.size == 0) {
            tvLabelResourses?.visibility = GONE
        } else {
            tvLabelResourses?.visibility = VISIBLE
        }

        if (listCategories.size == 0) {
            tvLabelCategories?.visibility = GONE
        } else {
            tvLabelCategories?.visibility = VISIBLE
        }
    }

    override fun itemDetail(postId: Int) {
        if (!isFromScan) {
            (activity as MainActivity).openInfoServiceFragment(listCategories.get(postId))
        } else {
            (activity as MainActivity).showScanFragment(null, listCategories.get(postId).id)
        }
    }

    override fun clickDetailResource(postId: String) {

        if (!isFromScan) {
            Toast.makeText(activity, "Definir acción para recursos", Toast.LENGTH_SHORT).show()
        } else {
            (activity as MainActivity).showScanFragment(postId, null)
        }

    }

    override fun clickScanresourse(postId: String) {

    }

    override fun clickScanCategory(postId: String) {

    }


    companion object {
        val TAG: String = "ResoursesProviderFragment"
    }

    override fun onResume() {
        super.onResume()

        if (!isFromScan) {
            (activity as MainActivity).setTextToolbar(
                getText(R.string.my_resourse).toString(),
                activity!!.resources.getColor(R.color.colorWhite)
            )
        } else {
            (activity as MainActivity).setTextToolbar(
                getText(R.string.count_resourses).toString(),
                activity!!.resources.getColor(R.color.colorWhite)
            )
        }




    }


}

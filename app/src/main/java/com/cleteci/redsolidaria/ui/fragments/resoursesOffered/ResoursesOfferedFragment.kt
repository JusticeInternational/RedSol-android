package com.cleteci.redsolidaria.ui.fragments.resoursesOffered


import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourseAdapter
import com.cleteci.redsolidaria.ui.adapters.CategoriesAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject


class ResoursesOfferedFragment : BaseFragment(), ResoursesOfferedContract.View,
    CategoriesAdapter.OnItemClickListener, ResourseAdapter.onItemClickListener {



    var isFromScan = false

    var tvLabelResourses: TextView? = null
    var tvLabelCategories: TextView? = null

    var rvResourses: RecyclerView? = null
    var rvResoursesGeneric: RecyclerView? = null
    var rvMyCategories: RecyclerView? = null
    var mAdapter: CategoriesAdapter? = null
    var resoursesAdapter: ResourseAdapter? = null
    var genericResoursesAdapter: ResourseAdapter? = null

    var fab: FloatingActionButton? = null
    private val listCategories = ArrayList<Category>()
    private val listResourses = ArrayList<Service>()
    private val listGenericResourses = ArrayList<Service>()


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
        rvResourses = rootView.findViewById(R.id.rvCategories)
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
        mAdapter = CategoriesAdapter(
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
        pending: List<Category>,
        services: List<Service>,
        genericServices: List<Service>
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
            (activity as MainActivity).openInfoFragment(listCategories.get(postId), null)
        } else {
            (activity as MainActivity).showScanFragment(null, listCategories.get(postId).id,listCategories.get(postId).name, true )
        }
    }

    override fun clickDetailResource(postId: Int, name: String, isGeneric:Boolean) {
        if (!isFromScan) {
            if(!isGeneric) {
                (activity as MainActivity).openInfoFragment(null, listResourses.get(postId))
            } else {
                (activity as MainActivity).openInfoFragment(null, listGenericResourses.get(postId))
            }
        } else {
            (activity as MainActivity).showScanFragment(listResourses.get(postId).id, null, name, isGeneric)
        }

    }

    override fun scanNoUserCategory(position: Int) {
        (activity as MainActivity).openScanNoUserFragment(null, listCategories[position].id, listCategories[position].name, false)
    }

    override fun scanNoUserResource(postId: String, name: String, isGeneric: Boolean) {
        (activity as MainActivity).openScanNoUserFragment(postId, null, name, isGeneric)

    }

    override fun clickScanresourse(postId: String) {


    }

    override fun clickScanCategory(postId: Int) {

    }


    companion object {
        val TAG: String = "ResoursesProviderFragment"
    }

    override fun onResume() {
        super.onResume()

        if (!isFromScan) {
            (activity as MainActivity).setTextToolbar(
                getText(R.string.my_resources).toString(),
                activity!!.resources.getColor(R.color.colorWhite)
            )
        } else {
            (activity as MainActivity).setTextToolbar(
                getText(R.string.count_resources).toString(),
                activity!!.resources.getColor(R.color.colorWhite)
            )
        }




    }


}

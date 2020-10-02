package com.cleteci.redsolidaria.ui.fragments.resoursesOffered


import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.ResourceCategory
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourseCategoryAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject


class ResoursesOfferedFragment : BaseFragment(), ResoursesOfferedContract.View,
    ResourseCategoryAdapter.onItemClickListener {


    var rvResourses: RecyclerView? = null
    var mAdapter: ResourseCategoryAdapter? = null
    var fab: FloatingActionButton? = null
    private val listResourses = ArrayList<ResourceCategory>()


    @Inject
    lateinit var presenter: ResoursesOfferedContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): ResoursesOfferedFragment {
        return ResoursesOfferedFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_resourses_offered, container, false)
        rvResourses = rootView.findViewById(R.id.rvResourses)
        fab=rootView.findViewById(R.id.fab)
        fab?.setOnClickListener{
            (activity as MainActivity).openCreateServiceFragment()
        }
        rvResourses?.setLayoutManager(
            LinearLayoutManager(getActivity())
        )
        mAdapter = ResourseCategoryAdapter(
            activity?.applicationContext, listResourses, this, 4
        )
        rvResourses?.setAdapter(mAdapter)
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

    override fun loadDataSuccess(pending: List<ResourceCategory>) {
        activity?.runOnUiThread(Runnable {
            listResourses.clear()
            listResourses.addAll(pending)
            mAdapter?.notifyDataSetChanged()
        })
    }

    override fun itemDetail(postId: Int) {
        (activity as MainActivity).openInfoServiceFragment(listResourses.get(postId))
    }

    companion object {
        val TAG: String = "ResoursesProviderFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.my_resourse).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )

    }


}

package com.cleteci.redsolidaria.ui.fragments.myResourses


import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.BaseApp

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.ResourseAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.fragments.servicedetail.ServiceDetailFragment
import javax.inject.Inject


class MyResoursesFragment : BaseFragment(), MyResoursesContract.View, ResourseAdapter.onItemClickListener {


    var mScrollView: NestedScrollView? = null

    var lyEmpty: LinearLayout? = null

    var rvPending: RecyclerView? = null
    var mAdapterPending: ResourseAdapter? = null
    private val listPending = ArrayList<Resource>()

    var rvSaved: RecyclerView? = null
    var mAdapterSaved: ResourseAdapter? = null
    private val listSaved = ArrayList<Resource>()

    var rvVolunteering: RecyclerView? = null
    var mAdapterVolunteering: ResourseAdapter? = null
    private val listVolunteering = ArrayList<Resource>()

    var rvUsed: RecyclerView? = null
    var mAdapterUsed: ResourseAdapter? = null
    private val listUsed = ArrayList<Resource>()


    @Inject
    lateinit var presenter: MyResoursesContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): MyResoursesFragment {
        return MyResoursesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_my_resources, container, false)

        mScrollView = rootView?.findViewById(R.id.mScrollView)

        lyEmpty = rootView?.findViewById(R.id.lyEmpty)

        rvPending = rootView?.findViewById(R.id.rvPending)
        rvPending?.setLayoutManager(LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false))
        mAdapterPending = ResourseAdapter(activity?.applicationContext, listPending, this, 2)
        rvPending?.setAdapter(mAdapterPending)

        rvSaved = rootView?.findViewById(R.id.rvSaved)
        rvSaved?.setLayoutManager(LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false))
        mAdapterSaved = ResourseAdapter(activity?.applicationContext, listSaved, this, 2)
        rvSaved?.setAdapter(mAdapterSaved)

        rvVolunteering = rootView?.findViewById(R.id.rvVolunteering)
        rvVolunteering?.setLayoutManager(LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false))
        mAdapterVolunteering = ResourseAdapter(activity?.applicationContext, listVolunteering, this, 2)
        rvVolunteering?.setAdapter(mAdapterVolunteering)

        rvUsed = rootView?.findViewById(R.id.rvUsed)
        rvUsed?.setLayoutManager(LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false))
        mAdapterUsed = ResourseAdapter(activity?.applicationContext, listUsed, this, 2)
        rvUsed?.setAdapter(mAdapterUsed)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
        initView()
        presenter.getData()
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

    companion object {
        val TAG: String = "MyResoursesFragment"
    }

    override fun loadDataSuccess(
        pending: List<Resource>,
        saved: List<Resource>,
        volunteer: List<Resource>,
        used: List<Resource>
    ) {
        listPending.clear()
        listPending.addAll(pending)
        mAdapterPending?.notifyDataSetChanged()

        listSaved.clear()
        listSaved.addAll(saved)
        mAdapterSaved?.notifyDataSetChanged()

        listUsed.clear()
        listUsed.addAll(used)
        mAdapterUsed?.notifyDataSetChanged()

        listVolunteering.clear()
        listVolunteering.addAll(volunteer)
        mAdapterVolunteering?.notifyDataSetChanged()
    }

    override fun clickDetailResource(postId: String) {
        ( activity as MainActivity).openServiceDetailFragment()
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.my_resourse).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )

        if (BaseApp.prefs.login_later) {

            mScrollView?.visibility = View.GONE
            lyEmpty?.visibility = View.VISIBLE


        } else {
            mScrollView?.visibility = View.VISIBLE
            lyEmpty?.visibility = View.GONE


        }
    }


}

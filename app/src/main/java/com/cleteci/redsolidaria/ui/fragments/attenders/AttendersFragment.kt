package com.cleteci.redsolidaria.ui.fragments.users


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.models.ResourceCategory
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject


class AttendersFragment : BaseFragment() , AttendersContract.View  {


    @Inject lateinit var presenter: AttendersContract.Presenter
     var mSectionsPagerAdapter:SectionsPagerAdapter? = null
    var catService: ResourceCategory? = null
    var service: Resource? = null
    var tabs: TabLayout?=null
    var mViewPager: ViewPager?=null
    private lateinit var rootView: View

    fun newInstance(service: Resource): AttendersFragment {

        val fragment = AttendersFragment()
        val args = Bundle()
        args.putSerializable("service", service)
        fragment.setArguments(args)
        return fragment

    }

    fun newInstance(category: ResourceCategory): AttendersFragment {
        val fragment = AttendersFragment()
        val args = Bundle()
        args.putSerializable("category", category)
        fragment.setArguments(args)
        return fragment
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null && arguments?.getSerializable("category") != null) {
            catService = arguments?.getSerializable("category") as ResourceCategory
        } else {
            service = arguments?.getSerializable("service") as Resource
        }
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_attenders, container, false)
        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager,  catService, service)
        tabs=rootView.findViewById(R.id.tabs)
        mViewPager=rootView.findViewById(R.id.container)
        mViewPager?.setCurrentItem(2, true)
        mViewPager?.setAdapter(mSectionsPagerAdapter)
        tabs?.setupWithViewPager(mViewPager)
        tabs?.setTabsFromPagerAdapter(mSectionsPagerAdapter)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
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

    companion object {
        val TAG: String = "AttendersFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(getText(R.string.attenders).toString(),activity!!.resources.getColor(R.color.colorWhite))

    }


    class SectionsPagerAdapter(fm: FragmentManager, catService: ResourceCategory?,service: Resource? ) : FragmentPagerAdapter(fm) {
       /* var catService:ResourceCategory?=catService
        var service:Resource?=service*/

        var frag1=AttendersListFragment().newInstance(1, service, catService)
        var frag2=AttendersListFragment().newInstance(2, service, catService)

        override fun getItem(position: Int): Fragment {
            Log.d("TAGI", "--"+position)
            when (position) {

                0 -> return  frag1
                else -> return frag2

            }

        }

        override fun getCount(): Int {

            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Registrados"
                1 -> return "No registrados"
            }
            return null
        }
    }



}

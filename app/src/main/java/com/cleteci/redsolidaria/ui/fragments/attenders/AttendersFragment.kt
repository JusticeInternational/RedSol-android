package com.cleteci.redsolidaria.ui.fragments.users


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cleteci.redsolidaria.BaseApp

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject


class AttendersFragment : BaseFragment() , AttendersContract.View  {


    @Inject lateinit var presenter: AttendersContract.Presenter
     var mSectionsPagerAdapter:SectionsPagerAdapter? = null
    var catService: Category? = null
    var service: Service? = null
    var tabs: TabLayout?=null
    var mViewPager: ViewPager?=null
    private lateinit var rootView: View

    fun newInstance(service: Service): AttendersFragment {

        val fragment = AttendersFragment()
        val args = Bundle()
        args.putSerializable("service", service)
        fragment.setArguments(args)
        return fragment

    }

    fun newInstance(category: Category): AttendersFragment {
        val fragment = AttendersFragment()
        val args = Bundle()
        args.putSerializable("category", category)
        fragment.setArguments(args)
        return fragment
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null && arguments?.getSerializable("category") != null) {
            catService = arguments?.getSerializable("category") as Category
        } else {
            service = arguments?.getSerializable("service") as Service
        }
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_attenders, container, false)

        tabs=rootView.findViewById(R.id.tabs)
        mViewPager=rootView.findViewById(R.id.container)

        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager,  catService, service)
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

    fun updateTabA(text: Int) {
        countA=text


        tabs?.getTabAt(0)?.setText( getString(R.string.registered)+ "("+countA+")")

    }

    fun updateTabB(text: Int) {

        countB=text
        tabs?.getTabAt(1)?.setText( getString(R.string.not_registered)+ "("+countB+")")

    }

    companion object {
        val TAG: String = "AttendersFragment"
        var countA:Int=0
        var countB:Int=0
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(getText(R.string.attenders).toString(),activity!!.resources.getColor(R.color.colorWhite))

    }


    class SectionsPagerAdapter(fm: FragmentManager, catService: Category?, service: Service? ) : FragmentPagerAdapter(fm) {
       /* var catService:ResourceCategory?=catService
        var service:Resource?=service*/

        var frag1=AttendersListFragment().newInstance(1, service, catService)
        var frag2=AttendersListFragment().newInstance(2, service, catService)

        override fun getItem(position: Int): Fragment {

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
                0 -> return  BaseApp.instance.getString(R.string.registered)+ "("+countA+")"
                1 -> return  BaseApp.instance.getString(R.string.registered)+ "("+countB+")"
            }
            return null
        }
    }



}

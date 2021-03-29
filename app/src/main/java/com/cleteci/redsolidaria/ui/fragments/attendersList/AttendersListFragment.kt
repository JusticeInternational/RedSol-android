package com.cleteci.redsolidaria.ui.fragments.users


import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.models.User
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.UserAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import javax.inject.Inject


class AttendersListFragment : BaseFragment(), AttendersListContract.View {


    var catService: Category? = null
    var service: Service? = null
    var type: Int? = null

    var rvUsers: RecyclerView? = null
    private val listUsers = ArrayList<User>()
    var usersAdapter: UserAdapter? = null

    @Inject
    lateinit var presenter: AttendersListContract.Presenter

    private lateinit var rootView: View

    fun newInstance(type: Int, serviceId: Service?, catId: Category?): AttendersListFragment {

        val fragment = AttendersListFragment()
        val args = Bundle()
        args.putInt("type", type)
        args.putSerializable("service", serviceId)
        args.putSerializable("cat", catId)
        fragment.setArguments(args)
        return fragment

        //return AttendersListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            type = arguments!!.getInt("type")
            if (arguments!!.getSerializable("service") != null) {
                service = arguments!!.getSerializable("service") as Service
            } else {
                catService = arguments!!.getSerializable("cat") as Category
            }

        }
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_attenders_list, container, false)

        rvUsers = rootView.findViewById(R.id.rvAttenders)

        rvUsers?.setLayoutManager(
            LinearLayoutManager(getActivity())
        )

        usersAdapter = UserAdapter(
            activity?.applicationContext, listUsers
        )

        rvUsers?.setAdapter(usersAdapter)

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
        if (service != null) {
            presenter.loadDataService(service!!.id, type!!)
        } else {
            presenter.loadDataCategory(catService!!.id, type!!)
        }

    }

    override fun showUsers(users: List<User>) {

        activity?.runOnUiThread(Runnable {
            listUsers.clear()
            listUsers.addAll(users)
            usersAdapter?.notifyDataSetChanged()
            val parentFrag = getParentFragment() as AttendersFragment
            if (type==1) {
               // AttendersFragment.countA =
                parentFrag.updateTabA(listUsers.size)
            } else {
                //AttendersFragment.countB= listUsers.size
                parentFrag.updateTabB(listUsers.size)
            }




        })

    }

    companion object {
        val TAG: String = "AttendersListFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.attenders).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )

    }


}

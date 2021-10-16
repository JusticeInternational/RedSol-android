package com.cleteci.redsolidaria.ui.fragments.attendersList


import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.models.User
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.adapters.UserAdapter
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.cleteci.redsolidaria.ui.fragments.users.AttendersFragment
import com.cleteci.redsolidaria.ui.fragments.users.AttendersListContract
import javax.inject.Inject


class AttendersListFragment : BaseFragment(), AttendersListContract.View {

    private var resourceId: String = ""
    private var resourceType: String = ""
    private var type: Int = 0
    var rvUsers: RecyclerView? = null
    private val listUsers = ArrayList<User>()
    var usersAdapter: UserAdapter? = null

    @Inject
    lateinit var presenter: AttendersListContract.Presenter

    private lateinit var rootView: View

    fun newInstance(type: Int, resourceId: String, resourceType: String): AttendersListFragment {
        val fragment = AttendersListFragment()
        val args = Bundle()
        args.putInt("type", type)
        args.putString("resourceId", resourceId)
        args.putString("resourceType", resourceType)
        fragment.setArguments(args)
        return fragment

        //return AttendersListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            type = arguments!!.getInt("type")
            resourceId = arguments!!.getString("resourceId", "")
            resourceType = arguments!!.getString("resourceType", "")
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

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.attenders).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )
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
        if (Resource.Type.valueOf(resourceType) == Resource.Type.CATEGORY) {
            presenter.loadDataCategory(resourceId!!, type)
        } else {
            presenter.loadDataService(resourceId!!, type)
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

        const val TAG: String = "AttendersListFragment"

    }

}

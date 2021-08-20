package com.cleteci.redsolidaria.ui.fragments.infoService

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import javax.inject.Inject

class InfoServiceFragment : BaseFragment(), InfoServiceContract.View {
    var catService: Category? = null
    var service: Service? = null
    var tvName: TextView? = null
    var totalServed: TextView? = null
    var ivService: ImageView? = null
    var btAttend: Button? = null


    @Inject
    lateinit var presenter: InfoServiceContract.Presenter

    private lateinit var rootView: View

    fun newInstance(catService1: Category?, service: Service?): InfoServiceFragment {
        var frag: InfoServiceFragment = InfoServiceFragment()
        var args = Bundle()
        if (catService1 != null) {
            args.putSerializable("category", catService1)
        } else {
            args.putSerializable("service", service)
        }
        frag.setArguments(args)

        return frag
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
        rootView = inflater.inflate(R.layout.fragment_info_service, container, false)

        ivService = rootView.findViewById(R.id.ivService)

        tvName = rootView.findViewById(R.id.tvOrganizationName)

        totalServed = rootView.findViewById(R.id.totalServed)

        btAttend = rootView.findViewById(R.id.btAttend)

        btAttend?.setOnClickListener {
            if (service!=null) {
                (activity as MainActivity).openAttendFragment(service!!)
            } else {
                (activity as MainActivity).openAttendFragment(catService!!)
            }

        }

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

    override fun init() {}

    private fun initView() {
        if (catService != null) {
            ivService?.setImageResource(catService!!.photo)
            tvName?.text = catService!!.name
            presenter.loadCategoryData(catService!!.id)
        } else {
            ivService?.setImageResource(service!!.category.iconId)
            tvName?.text = service!!.name
            presenter.loadServiceData(service!!.id)
        }
    }

    override fun loadDataSuccess(total: Int) {
        activity?.runOnUiThread(Runnable {
            totalServed?.text = total.toString()
        })
    }

    companion object {
        val TAG: String = "InfoServiceFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.info_services).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )

    }
}

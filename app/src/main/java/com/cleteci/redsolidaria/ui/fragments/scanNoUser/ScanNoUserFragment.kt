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
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.models.ResourceCategory
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import javax.inject.Inject

class ScanNoUserFragment : BaseFragment(), ScanNoUserContract.View {
    var catService: ResourceCategory? = null
    var service: Resource? = null



    @Inject
    lateinit var presenter: ScanNoUserContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): ScanNoUserFragment {
        var frag: ScanNoUserFragment = ScanNoUserFragment()
        var args = Bundle()
      /*  if (catService1 != null) {
            args.putSerializable("category", catService1)
        } else {
            args.putSerializable("service", service)
        }*/
        frag.setArguments(args)

        return frag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* if (arguments != null && arguments?.getSerializable("category") != null) {
            catService = arguments?.getSerializable("category") as ResourceCategory
        } else {
            service = arguments?.getSerializable("service") as Resource
        }*/
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_scan_no_user, container, false)



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

    }

    override fun loadDataSuccess(total: Int) {

    }

    companion object {
        val TAG: String = "ScanNoUserFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.count_attention).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )

    }
}

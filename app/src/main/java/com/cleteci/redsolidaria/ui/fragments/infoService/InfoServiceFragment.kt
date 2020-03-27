package com.cleteci.redsolidaria.ui.fragments.infoService

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.models.ResourseCategory
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.google.gson.Gson
import javax.inject.Inject

class InfoServiceFragment : BaseFragment() , InfoServiceContract.View  {
    var catService:ResourseCategory?=null
    var tvName:TextView?=null
    var ivService:ImageView?=null

    @Inject
    lateinit var presenter: InfoServiceContract.Presenter

    private lateinit var rootView: View

    fun newInstance(catService1:ResourseCategory): InfoServiceFragment {
        var frag: InfoServiceFragment= InfoServiceFragment()
        var args = Bundle()
        args.putSerializable("category", catService1)
        frag.setArguments(args)

        //catService=catService1
        return frag
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
             catService = arguments?.getSerializable("category") as ResourseCategory

        }
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_info_service, container, false)

        ivService=rootView.findViewById(R.id.ivService)

        tvName=rootView.findViewById(R.id.tvName)



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

        ivService?.setImageResource(catService!!.photo)

        tvName?.setText(catService!!.name)
    }

    companion object {
        val TAG: String = "InfoServiceFragment"
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setTextToolbar(getText(R.string.info_services).toString(),activity!!.resources.getColor(R.color.colorWhite))

    }



}

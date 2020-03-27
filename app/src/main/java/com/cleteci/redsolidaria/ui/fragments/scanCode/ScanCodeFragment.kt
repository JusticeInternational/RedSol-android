package com.cleteci.redsolidaria.ui.fragments.scanCode


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import com.cleteci.redsolidaria.BaseApp

import com.cleteci.redsolidaria.R

import com.cleteci.redsolidaria.di.component.DaggerFragmentComponent
import com.cleteci.redsolidaria.di.module.FragmentModule
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.*
import com.rey.material.widget.ProgressView
import javax.inject.Inject


class ScanCodeFragment : BaseFragment(), ScanCodeContract.View {

    var capture: CaptureManager? = null
    var barcodeScannerView: DecoratedBarcodeView? = null
    var beepManager: BeepManager? = null
    var viewFinder: ViewfinderView? = null
    var lyOverView: RelativeLayout? = null
    var callback: BarcodeCallback? = null

    var btSend: Button? = null


    @Inject
    lateinit var presenter: ScanCodeContract.Presenter

    private lateinit var rootView: View

    fun newInstance(): ScanCodeFragment {
        return ScanCodeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_scan_code, container, false)

        btSend = rootView?.findViewById(R.id.btSend);

        btSend?.setOnClickListener {
            Toast.makeText(activity!!, getString(R.string.progress), Toast.LENGTH_SHORT).show()
        }


        beepManager = BeepManager(activity)

        beepManager?.setBeepEnabled(false)
        beepManager?.setVibrateEnabled(true)

        //Initialize barcode scanner view
        barcodeScannerView = rootView.findViewById(R.id.zxing_barcode_scanner)

        lyOverView = rootView.findViewById(R.id.lyOverView)

        viewFinder = rootView.findViewById(R.id.zxing_viewfinder_view)

        callback = object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {

                beepManager?.playBeepSoundAndVibrate()

                Toast.makeText(activity, getString(R.string.progress), Toast.LENGTH_SHORT).show()

            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {

            }
        }


        barcodeScannerView?.decodeSingle(callback)

        capture = CaptureManager(activity, barcodeScannerView)
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
        val TAG: String = "ScanCodeFragment"
    }

    override fun onResume() {
        super.onResume()
        capture?.onResume()
        (activity as MainActivity).setTextToolbar(
            getText(R.string.scan_qr).toString(),
            activity!!.resources.getColor(R.color.colorWhite)
        )

    }

    override fun onPause() {
        super.onPause()
        capture?.onPause();
    }

    override fun onDestroy() {
        super.onDestroy()
        capture?.onDestroy();
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capture?.onSaveInstanceState(outState)
    }

}

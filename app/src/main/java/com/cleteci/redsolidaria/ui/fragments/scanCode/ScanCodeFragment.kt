package com.cleteci.redsolidaria.ui.fragments.scanCode

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.ui.activities.main.MainActivity
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.*
import org.koin.android.ext.android.inject

class ScanCodeFragment : BaseFragment(), ScanCodeContract.View {

    var serviceID: String? = null
    var catID: String? = null
    var name: String? = null
    var isGeneric: Boolean = false
    var barcodeScannerView: DecoratedBarcodeView? = null
    var beepManager: BeepManager? = null
    var viewFinder: ViewfinderView? = null
    var lyOverView: RelativeLayout? = null
    var callback: BarcodeCallback? = null

    var btSend: Button? = null
    var zxing_status_view: TextView? = null

    private val presenter: ScanCodeContract.Presenter by inject()
    private lateinit var rootView: View

    fun newInstance(serviceID: String?, catId: String?, name: String?, isGeneric: Boolean): ScanCodeFragment {
        val fragment = ScanCodeFragment()
        val args = Bundle()
        args.putString("serviceID", serviceID)
        args.putString("catID", catId)
        args.putString("name", name)
        args.putBoolean("isGeneric", isGeneric)
        fragment.arguments = args
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            serviceID = it.getString("serviceID")
            catID = it.getString("catID")
            name = it.getString("name")
            isGeneric = it.getBoolean("isGeneric")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_scan_code, container, false)

        btSend = rootView.findViewById(R.id.btSend)
        zxing_status_view = rootView.findViewById(R.id.zxing_status_view)

        btSend?.setOnClickListener {
            context?.let { ctx ->
                Toast.makeText(ctx, getString(R.string.progress), Toast.LENGTH_SHORT).show()
            }
        }

        beepManager = BeepManager(activity).apply {
            isBeepEnabled = false
            isVibrateEnabled = true
        }

        barcodeScannerView = rootView.findViewById(R.id.zxing_barcode_scanner)
        lyOverView = rootView.findViewById(R.id.lyOverView)
        viewFinder = rootView.findViewById(R.id.zxing_viewfinder_view)

        callback = object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                val scannedText = result.text
                if (scannedText.isNullOrBlank()) {
                    return
                }

                beepManager?.playBeepSoundAndVibrate()

                val sId = serviceID
                val cId = catID
                val rName = name.orEmpty()

                if (sId != null) {
                    presenter.countService(scannedText, sId, rName, isGeneric)
                } else if (cId != null) {
                    presenter.countCategory(scannedText, cId, rName)
                }
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {

            }
        }

        settingDecode()

        return rootView
    }

    fun settingDecode() {
        val act = activity ?: return
        act.runOnUiThread {
            if (!isAdded) return@runOnUiThread
            val ctx = context ?: return@runOnUiThread
            if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                barcodeScannerView?.resume()
                barcodeScannerView?.decodeSingle(callback)
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                settingDecode()
            } else {
                context?.let { ctx ->
                    Toast.makeText(
                        ctx,
                        getString(R.string.camera_permission_required),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        barcodeScannerView?.pause()
        presenter.unsubscribe()
    }

    override fun init() {

    }

    private fun initView() {

    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            barcodeScannerView?.resume()
        }

        val pronoun: String
        val offered: String
        val title: String

        if (serviceID != null) {
            pronoun = getString(R.string.the_2)
            offered = getString(R.string.offered_2)
            title = if (isGeneric) {
                name?.lowercase().orEmpty()
            } else {
                getString(R.string.service).lowercase() + " " + name.orEmpty()
            }
        } else {
            pronoun = getString(R.string.the_1)
            offered = getString(R.string.offered_1)
            title = getString(R.string.category).lowercase() + " " + name.orEmpty()
        }

        zxing_status_view?.text = String.format(
            BaseApp.instance.resources.getString(R.string.to_scan),
            pronoun,
            title,
            offered
        )

        (activity as? MainActivity)?.setTextToolbar(
            getText(R.string.scan_qr).toString() + " " + title,
            ContextCompat.getColor(requireContext(), R.color.colorWhite)
        )
    }

    override fun onPause() {
        super.onPause()
        barcodeScannerView?.pause()
    }

    override fun showErrorMsg(msg: String) {
        activity?.runOnUiThread {
            if (isAdded) {
                showAlert(R.drawable.ic_error, msg)
            }
        }
    }

    override fun showSuccessMsg(msg: String) {
        activity?.runOnUiThread {
            if (isAdded) {
                showAlert(R.drawable.ic_check_green, msg)
            }
        }
    }

    fun showAlert(icon: Int, msg: String) {
        val act = activity ?: return
        if (!isAdded || act.isFinishing) return

        val dialog = Dialog(act)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.comp_alert_succes_suggest_resource)

        val ivIcon = dialog.findViewById<ImageView>(R.id.ivIcon)
        val tvAlertMsg = dialog.findViewById<TextView>(R.id.tvAlertMsg)
        ivIcon.setImageResource(icon)
        tvAlertMsg.text = msg

        val yesBtn = dialog.findViewById<Button>(R.id.btCont)
        yesBtn.setOnClickListener {
            dialog.dismiss()
            settingDecode()
        }

        dialog.show()
    }

    companion object {
        const val TAG: String = "ScanCodeFragment"
        const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }
}

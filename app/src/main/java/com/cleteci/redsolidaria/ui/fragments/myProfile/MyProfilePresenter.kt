package com.cleteci.redsolidaria.ui.fragments.myProfile

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.WHITE
import com.cleteci.redsolidaria.BaseApp
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ogulcan on 07/02/2018.
 */
class MyProfilePresenter: MyProfileContract.Presenter {


    private val subscriptions = CompositeDisposable()
    private lateinit var view: MyProfileContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MyProfileContract.View) {
        this.view = view
        view.init() // as default
    }

    override fun getQR() {
        try {
            val bitmap = textToQRBitmap(BaseApp.sharedPreferences.userSaved.toString())
            if (bitmap!=null){
                view.showQR(bitmap)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun textToQRBitmap(myCodeText: String): Bitmap? {
        val writer = QRCodeWriter()
        var bmp: Bitmap? = null
        try {
            val bitMatrix = writer.encode(myCodeText, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp!!.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else WHITE)
                }
            }

        } catch (e: WriterException) {
            e.printStackTrace()
        }

        return bmp
    }


}
package com.cleteci.redsolidaria.util

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.cleteci.redsolidaria.R
import kotlinx.android.synthetic.main.fragment_resources_offered.*


class Constants {
    companion object {
        const val BASE_URL = "http://redsol.eastus.cloudapp.azure.com/graphql"
        const val ORGANIZATION_EMAIL = "redsol.app@gmail.com"
        const val ORGANIZATION_PHONE = "0123456789"
        const val SLASH = "/"
    }
}

fun openDialerClient(context: Context, phone: String) {
    try {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:${phone}")
        if (callIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(callIntent)
        }
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(
            context, context.getString(R.string.error_email_app_not_found),
            Toast.LENGTH_SHORT
        ).show()
    }


}

fun openEmailClient(context: Context, email: String) {
    try {
        val emailIntent =
            Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "")

        if (emailIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(
                Intent.createChooser(
                    emailIntent,
                    context.getString(R.string.select_email_client)
                )
            )
        }
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(
            context, context.getString(R.string.error_email_app_not_found),
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun openBrowser(context: Context, link: String) {
    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(link)
    )
    context.startActivity(browserIntent)
}

fun openGoogleMaps(context: Context, location: String, packageManager: PackageManager) {
    val gmmIntentUri = Uri.parse(location)
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    mapIntent.resolveActivity(packageManager)?.let {
        context.startActivity(mapIntent)
    }
}

fun showInfoDialog(context: Context?, title: String, msg: String) {
    val alertDialog: AlertDialog? = context?.let {
        val builder = AlertDialog.Builder(it)
        builder.apply {
            setPositiveButton(
                R.string.ok
            ) { dialog, _ ->
                dialog.dismiss()
            }
        }
        builder.create()
    }
    alertDialog?.let {
        it.setTitle(title)
        it.setMessage(msg)
        it.show()
    }
}
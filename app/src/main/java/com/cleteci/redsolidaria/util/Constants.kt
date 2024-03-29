package com.cleteci.redsolidaria.util

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.Spanned
import android.util.Patterns
import android.widget.Toast
import com.cleteci.redsolidaria.R
import java.util.*
import kotlin.collections.ArrayList


class Constants {
    companion object {
        const val BASE_URL = "http://redsol.eastus.cloudapp.azure.com/graphql"
        const val ORGANIZATION_EMAIL = "redsol.app@gmail.com"
        const val ORGANIZATION_PHONE = "0123456789"
        const val SLASH = "/"
        const val DEFAULT_PASSWORD = "1234"
        const val DEFAULT_PASSWORD_LENGTH = 4
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

fun openEmailClient(context: Context, email: String, subject: String = "", body: Spanned? = null) {
    try {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (body != null) {
            emailIntent.putExtra(Intent.EXTRA_TEXT, body)
        }

        if (emailIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(
                Intent.createChooser(
                    emailIntent,
                    context.getString(R.string.select_email_client)
                )
            )
        } else {
            Toast.makeText(
                context, context.getString(R.string.error_email_app_not_found),
                Toast.LENGTH_SHORT
            ).show()
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

fun getCountries() : ArrayList<String> {
    val locales: Array<Locale> = Locale.getAvailableLocales()
    val countries = ArrayList<String>()
    for (locale in locales) {
        val country: String = locale.displayCountry
        if (country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)) {
            countries.add(country)
        }
    }
    countries.sort()
    return countries
}

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence?.isValidPhone() = !isNullOrEmpty() && Patterns.PHONE.matcher(this).matches()
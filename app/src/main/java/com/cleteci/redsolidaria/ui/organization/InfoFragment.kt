package com.cleteci.redsolidaria.ui.organization

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Organization
import com.cleteci.redsolidaria.util.*
import kotlinx.android.synthetic.main.fragment_organization_info.*


class InfoFragment(private val organization: Organization?) : Fragment(), View.OnClickListener {

    private lateinit var attributes: HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       if (organization == null)
            return
        attributes = hashMapOf(
            "schedule" to organization.schedule,
            "location" to organization.location,
            "page" to organization.webPage,
            "phone" to organization.phone,
            "email" to organization.user.email)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_organization_info, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (BaseApp.prefs.is_provider_service) {
            planLayout.visibility = View.VISIBLE
            btChangePlan.setOnClickListener {
                Toast.makeText(context,"Mostrar cambiar plan", Toast.LENGTH_LONG).show()
            }
        } else {
            planLayout.visibility = View.GONE
        }
        setupAttributes()
    }

    private fun setupAttributes() {
        for (key in attributes.keys) {
            var iconId= R.drawable.ic_time_24
            var label: String = ""
            when (key) {
                "schedule" -> {
                    iconId = R.drawable.ic_time_24
                    label = getString(R.string.schedule)
                }
                "location" -> {
                    iconId = R.drawable.ic_outline_location_24
                    label = getString(R.string.location)
                }
                "page" -> {
                    iconId = R.drawable.ic_link_24
                    label = getString(R.string.web_site)
                }
                "phone" -> {
                    iconId = R.drawable.ic_filled_phone_24
                    label = getString(R.string.phone)
                }
                "email" -> {
                    iconId = R.drawable.ic_filled_email_24
                    label = getString(R.string.email)
                }
            }
            linearAttributes.addView( getTextView(key, label, attributes[key]!!, iconId))
            linearAttributes.addView(getDivider())
        }
    }

    private fun getTextView(key: String, label: String, text: String, iconId: Int): TextView {
        val textView = TextView(context)
        val padding = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        textView.setPadding(padding,0,padding,0)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.BodyDefault)
        } else {
            textView.setTextAppearance(activity, R.style.BodyDefault)
        }

        textView.text = "$label: $text"
        textView.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_info_item)
        textView.setCompoundDrawablesWithIntrinsicBounds(0,0, iconId, 0)
        textView.setOnClickListener(this)
        textView.tag = key
        return textView
    }

    private fun getDivider(): View {
        val view = View(context)
        view.setBackgroundColor(resources.getColor(R.color.colorGrey))
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_divider)
        return view
    }

    override fun onClick(view: View?) {
        when ((view as TextView).tag) {
            "location" -> {
                activity?.packageManager?.let { openGoogleMaps(requireContext(),
                    "geo:${organization!!.lat}, ${organization.lng}", it) }
            }
            "page" -> {
                openBrowser(requireContext(), organization!!.webPage)
            }
            "phone" -> {
                openDialerClient(requireContext(), organization!!.phone)
            }
            "email" -> {
                openEmailClient(requireContext(), organization!!.user.email)
            }
        }
    }
}
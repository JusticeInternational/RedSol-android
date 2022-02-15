package com.cleteci.redsolidaria.ui.organizationProfile

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
import com.cleteci.redsolidaria.util.SharedPreferences.Companion.getOrganizationAttributes
import kotlinx.android.synthetic.main.fragment_organization_info.*


class InfoFragment : Fragment(), View.OnClickListener {

    private lateinit var attributes: HashMap<String, String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        attributes = getOrganizationAttributes()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_organization_info, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (BaseApp.sharedPreferences.isProviderService) {
            planLayout.visibility = View.VISIBLE
            btChangePlan.setOnClickListener {
                Toast.makeText(context, getString(R.string.in_build), Toast.LENGTH_LONG).show()
            }
        } else {
            planLayout.visibility = View.GONE
        }
        setupAttributes()
    }

    private fun setupAttributes() {
        for (key in attributes.keys) {
            var iconId = R.drawable.ic_time_24
            var label = ""
            when (key) {
                Organization.Attribute.SCHEDULE.name -> {
                    iconId = R.drawable.ic_time_24
                    label = getString(R.string.schedule)
                }
                Organization.Attribute.LOCATION.name -> {
                    iconId = R.drawable.ic_outline_location_24
                    label = getString(R.string.location)
                }
                Organization.Attribute.PAGE.name -> {
                    iconId = R.drawable.ic_link_24
                    label = getString(R.string.web_site)
                }
                Organization.Attribute.PHONE.name -> {
                    iconId = R.drawable.ic_filled_phone_24
                    label = getString(R.string.phone)
                }
                Organization.Attribute.EMAIL.name -> {
                    iconId = R.drawable.ic_filled_email_24
                    label = getString(R.string.email)
                }
            }
            if (label.isNotEmpty()) {
                linearAttributes.addView(getTextView(key, label, attributes[key]!!, iconId))
                linearAttributes.addView(getDivider())
            }
        }
    }

    private fun getTextView(key: String, label: String, text: String, iconId: Int): TextView {
        val textView = TextView(context)
        val padding = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        textView.setPadding(padding, 0, padding, 0)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.BodyDefault)
        } else {
            textView.setTextAppearance(activity, R.style.BodyDefault)
        }

        textView.text = "$label: $text"
        textView.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_info_item)
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconId, 0)
        textView.setOnClickListener(this)
        textView.tag = key
        return textView
    }

    private fun getDivider(): View {
        val view = View(context)
        view.setBackgroundColor(resources.getColor(R.color.colorGrey))
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        view.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_divider)
        return view
    }

    override fun onClick(view: View?) {
        when ((view as TextView).tag) {
            Organization.Attribute.LOCATION.name -> {
                val lat = attributes[Organization.Attribute.LAT.name]
                val lng = attributes[Organization.Attribute.LNG.name]
                if (!lat.isNullOrEmpty() && !lng.isNullOrEmpty()) {
                    activity?.packageManager?.let {
                        openGoogleMaps(
                            requireContext(),
                            "geo:$lat, $lng", it
                        )
                    }
                }
            }
            Organization.Attribute.PAGE.name -> {
                attributes[Organization.Attribute.PAGE.name]?.let {
                    openBrowser(
                        requireContext(),
                        it
                    )
                }
            }
            Organization.Attribute.PHONE.name -> {
                attributes[Organization.Attribute.PHONE.name]?.let {
                    openDialerClient(
                        requireContext(),
                        it
                    )
                }
            }
            Organization.Attribute.EMAIL.name -> {
                attributes[Organization.Attribute.EMAIL.name]?.let {
                    openEmailClient(
                        requireContext(),
                        it
                    )
                }
            }
        }
    }
}
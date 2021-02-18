package com.cleteci.redsolidaria.ui.organization

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cleteci.redsolidaria.R
import kotlinx.android.synthetic.main.fragment_organization_info.*


class InfoFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private val attributes = hashMapOf(
        "schedule" to "Horario: 8:00 am - 5:00 pm",
        "location" to "Ubicación: Santa Clara",
        "page" to "Página Web: www.scvmc.com",
        "phone" to "Teléfono: +984651384951",
        "email" to "Correo: scvmc@correo.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_organization_info, container, false)

//        pageViewModel.text.observe(viewLifecycleOwner, Observer<String> {
//            etTitle.text = it
//        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAttributes()
    }

    private fun setupAttributes() {
        for (key in attributes.keys) {
            var iconId= R.drawable.ic_time_24
            when (key) {
                "schedule" -> {
                    iconId = R.drawable.ic_time_24
                }
                "location" -> {
                    iconId = R.drawable.ic_outline_location_24
                }
                "page" -> {
                    iconId = R.drawable.ic_link_24
                }
                "phone" -> {
                    iconId = R.drawable.ic_filled_phone_24
                }
                "email" -> {
                    iconId = R.drawable.ic_filled_email_24
                }
            }
            linearAttributes.addView(getTextView(attributes[key]!!, iconId))
            linearAttributes.addView(getDivider())
        }
    }

    private fun getTextView(text: String, iconId: Int): TextView {
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

        textView.text = text
        textView.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_info_item)
        textView.setCompoundDrawablesWithIntrinsicBounds(0,0, iconId, 0)
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

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): InfoFragment {
            return InfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}
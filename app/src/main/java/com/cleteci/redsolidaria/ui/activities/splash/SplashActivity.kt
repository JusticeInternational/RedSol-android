package com.cleteci.redsolidaria.ui.activities.splash


import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.di.component.DaggerActivityComponent

import com.cleteci.redsolidaria.di.module.ActivityModule
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

import javax.inject.Inject
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.afollestad.viewpagerdots.DotsIndicator
import com.cleteci.redsolidaria.ui.activities.login.LoginActivity


/**
 * Created by ogulcan on 07/02/2018.
 */
class SplashActivity : AppCompatActivity(), SplashContract.View {

    /// var mListener: OnFragmentInteractionListener? = null
    var dots: DotsIndicator? = null

    var btNext: Button? = null
    var viewPager: ViewPager? = null
    var myViewPagerAdapter: MyViewPagerAdapter? = null

    private var layouts: IntArray? =
        intArrayOf(R.layout.fragment_splash_1, R.layout.fragment_splash_2, R.layout.fragment_splash_3);


    @Inject
    lateinit var presenter: SplashContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        injectDependency()

        presenter.attach(this)
    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        activityComponent.inject(this)
    }


    override fun init() {

        CalligraphyConfig.initDefault(
            CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )

        viewPager = findViewById(R.id.viewPager)
        btNext = findViewById(R.id.btNext)

        myViewPagerAdapter = MyViewPagerAdapter()
        viewPager!!.setAdapter(myViewPagerAdapter)
        viewPager!!.addOnPageChangeListener(viewPagerPageChangeListener)
        dots = findViewById(R.id.dots)

        dots!!.attachViewPager(viewPager)

        btNext!!.setOnClickListener {
            if (viewPager!!.currentItem != 2) {
                viewPager!!.setCurrentItem(viewPager!!.currentItem + 1, true)
            } else {
                val intent = Intent(this, LoginActivity::class.java)

                startActivity(intent)

                finish()
            }
        }
    }

    internal var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                // addBottomDots(position)

            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(arg0: Int) {

            }
        }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    /**
     * View pager adapter
     */
    inner class MyViewPagerAdapter : PagerAdapter() {

        private var layoutInflater: LayoutInflater? = null


        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

            val view = layoutInflater!!.inflate(layouts!!.get(position), container, false)
            container.addView(view)

            return view
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun getCount(): Int {
            return layouts!!.size
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

}
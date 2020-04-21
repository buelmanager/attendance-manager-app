package com.buel.holyhelpers.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonData.selectedDay
import com.buel.holyhelpers.data.CommonData.selectedDayOfWeek
import com.buel.holyhelpers.data.CommonData.selectedDays
import com.buel.holyhelpers.data.CommonData.selectedYear
import com.buel.holyhelpers.view.viewPage.CreateViewPageAdater
import com.buel.holyhelpers.view.viewPage.briefingPageViewFragment.AbstBriefingFragment
import com.commonLib.Common
import com.commonLib.MaterialDailogUtil.Companion.datePickerDialog
import com.commonLib.MaterialDailogUtil.Companion.showSingleChoice
import com.commonLib.MaterialDailogUtil.OnDialogSelectListner
import com.google.android.material.tabs.TabLayout
import com.orhanobut.logger.LoggerHelper

/**
 * Created by 19001283 on 2018-07-19.
 */
object BriefingViewPager {
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: FragmentPagerAdapter
    private var context: Context? = null
    fun setMainViewPage(context: Context, pageAdapterNum: Int) {
        BriefingViewPager.context = context
        val rootView = Common.getRootView(context)
        // Find the view pager that will allow the user to swipe between fragments
        viewPager = rootView.findViewById(R.id.mainActivity_viewpager)
        // Create an adapter that knows which fragment should be shown on each page
        pagerAdapter = CreateViewPageAdater.onCreateFragmentAdapter(context, (context as AppCompatActivity).supportFragmentManager, pageAdapterNum)
        // Set the adapter onto the view pager
        viewPager.setAdapter(pagerAdapter)
        // Give the TabLayout the ViewPager
        val tabLayout: TabLayout = rootView.findViewById(R.id.mainActivity_tablayout)
        tabLayout.setupWithViewPager(viewPager)
        viewPager.invalidate()
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) { //viewPager.getAdapter().notifyDataSetChanged();
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        pagerAdapter.notifyDataSetChanged()
    }

    fun setDataAndTime(dialogSelectListner: OnDialogSelectListner) {
        datePickerDialog(
                context!!,
                object : OnDialogSelectListner {
                    override fun onSelect(s: String) {
                        LoggerHelper.d(""+ selectedYear + "/" +
                                CommonData.selectedMonth + "/" +
                                selectedDay + "/" +
                                selectedDayOfWeek)
                        showSingleChoice(
                                context!!,
                                R.array.days_option,
                                object : OnDialogSelectListner {
                                    override fun onSelect(s: String) {
                                        selectedDays = s.toInt()
                                        LoggerHelper.d("setDataAndTime OnDialogSelectListner", "CommonData.getSelectedDays() : $selectedDays")
                                        val fragment = Common.getCurrentFragment(viewPager, pagerAdapter)
                                        pagerAdapter!!.notifyDataSetChanged()
                                        (fragment as AbstBriefingFragment).getAttandData()
                                        dialogSelectListner.onSelect("onComplete")
                                    }
                                })
                    }
                }
        )
    }

    fun getViewPager(): ViewPager? {
        return viewPager
    }

    fun setViewPager(viewPager: ViewPager?) {
        BriefingViewPager.viewPager = viewPager!!
    }

    fun getPagerAdapter(): FragmentPagerAdapter? {
        return pagerAdapter
    }

    fun setPagerAdapter(pagerAdapter: FragmentPagerAdapter?) {
        BriefingViewPager.pagerAdapter = pagerAdapter!!
    }
}
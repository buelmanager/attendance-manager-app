package com.buel.holyhelpers.view;

import android.content.Context;
import android.view.View;

import com.buel.holyhelpers.R;
import com.buel.holyhelpers.data.CommonData;
import com.buel.holyhelpers.view.viewPage.CreateViewPageAdater;
import com.buel.holyhelpers.view.viewPage.briefingPageViewFragment.AbstBriefingFragment;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.google.android.material.tabs.TabLayout;
import com.orhanobut.logger.LoggerHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by 19001283 on 2018-07-19.
 */

public class BriefingViewPager  {

    private static ViewPager viewPager = null;
    private static FragmentPagerAdapter pagerAdapter = null;
    private static Context context = null;

    public static void setMainViewPage(Context context, int pageAdapterNum) {

        BriefingViewPager.context = context;
        View rootView = Common.getRootView(context);
        // Find the view pager that will allow the user to swipe between fragments

        viewPager = rootView.findViewById(R.id.mainActivity_viewpager);
        // Create an adapter that knows which fragment should be shown on each page

        pagerAdapter = CreateViewPageAdater.onCreateFragmentAdapter(context, ((AppCompatActivity) context).getSupportFragmentManager(), pageAdapterNum);

        // Set the adapter onto the view pager
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = rootView.findViewById(R.id.mainActivity_tablayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.invalidate();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pagerAdapter.notifyDataSetChanged();
    }

    public static void setDataAndTime(MaterialDailogUtil.OnDialogSelectListner dialogSelectListner) {
        MaterialDailogUtil.Companion.datePickerDialog(
                context,
                s -> {

                    LoggerHelper.d(CommonData.getSelectedYear() + "/" +
                            CommonData.getSelectedMonth() + "/" +
                            CommonData.getSelectedDay() + "/" +
                            CommonData.getSelectedDayOfWeek());

                    MaterialDailogUtil.Companion.showSingleChoice(
                            context,
                            R.array.days_option,
                            s1 -> {
                                CommonData.setSelectedDays(Integer.parseInt(s1));
                                LoggerHelper.d("setDataAndTime OnDialogSelectListner" ,"CommonData.getSelectedDays() : " + CommonData.getSelectedDays());

                                Fragment fragment = Common.getCurrentFragment(viewPager, pagerAdapter);
                                pagerAdapter.notifyDataSetChanged();
                                ((AbstBriefingFragment) fragment).getAttandData();
                                dialogSelectListner.onSelect("onComplete");
                            });
                }
        );
    }


    public static ViewPager getViewPager() {
        return viewPager;
    }

    public static void setViewPager(ViewPager viewPager) {
        BriefingViewPager.viewPager = viewPager;
    }

    public static FragmentPagerAdapter getPagerAdapter() {
        return pagerAdapter;
    }

    public static void setPagerAdapter(FragmentPagerAdapter pagerAdapter) {
        BriefingViewPager.pagerAdapter = pagerAdapter;
    }
}

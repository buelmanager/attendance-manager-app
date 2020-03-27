package com.buel.holyhelpers.view.viewPage.samplePageViewFragment;

import android.content.Context;

import com.buel.holyhelpers.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by blue7 on 2018-05-14.
 */

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SampleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ViewPageCommonFragment(String.valueOf(R.layout.sample_page_view_fragment));
        } else if (position == 1){
            return new ViewPageCommonFragment(String.valueOf(R.layout.sample_page_view_fragment));
        } else if (position == 2){
            return new ViewPageCommonFragment(String.valueOf(R.layout.sample_page_view_fragment));
        } else {
            return new ViewPageCommonFragment(String.valueOf(R.layout.sample_page_view_fragment));
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 4;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.corp_setting_step1);
            case 1:
                return mContext.getString(R.string.corp_setting_step2);
            case 2:
                return mContext.getString(R.string.corp_setting_step3);
            case 3:
                return mContext.getString(R.string.corp_setting_step4);
            default:
                return null;
        }
    }

}
package com.buel.holyhelpers.view.viewPage.mainPageViewFragment;

import android.content.Context;

import com.buel.holyhelpers.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by blue7 on 2018-05-14.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MainFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new main1PageFragment();
        } else if (position == 1){
            return new main1PageFragment();
        } else if (position == 2){
            return new main1PageFragment();
        } else {
            return new main1PageFragment();
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 1;
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
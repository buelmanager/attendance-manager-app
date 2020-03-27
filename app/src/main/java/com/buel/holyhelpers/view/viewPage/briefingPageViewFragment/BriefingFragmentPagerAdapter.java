package com.buel.holyhelpers.view.viewPage.briefingPageViewFragment;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.buel.holyhelpers.R;

/**
 * Created by blue7 on 2018-05-14.
 */

public class BriefingFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public BriefingFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    //return new GroupBriefingFragment();
    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TeamBriefingRecyclerFragment();
        } else if (position == 1){
            return new YearBriefingRecyclerFragment();
        } else if (position == 2){
            return new DetailBriefingFragment();
        } else if (position == 3){
            return null;
        }  else{
            return null;
        }
    }


    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.briefing_step1);
            case 1:
                return mContext.getString(R.string.briefing_step2);
            case 2:
                return mContext.getString(R.string.briefing_step3);
            case 3:
                return mContext.getString(R.string.briefing_step4);
            default:
                return null;
        }
    }

    public interface OnChangedListener{
        public void onChanged();
    }
}
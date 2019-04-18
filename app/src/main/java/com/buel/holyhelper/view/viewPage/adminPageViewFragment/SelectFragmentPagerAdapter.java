package com.buel.holyhelper.view.viewPage.adminPageViewFragment;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.appcompat.app.AppCompatActivity;

import com.buel.holyhelper.R;
import com.orhanobut.logger.LoggerHelper;

/**
 * Created by blue7 on 2018-05-14.
 */

public class SelectFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SelectFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    public void  clearAll() //Clear all page
    {
        LoggerHelper.d("s clearAll" , "s clearAll");
        LoggerHelper.d("s getCount" , getCount());
        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        if (fragmentManager.getFragments() != null && getCount() > 0) {
            for (int i = 0; i < getCount(); i++) {
                Fragment mFragment = getItem(i);
                if (mFragment != null) {

                    LoggerHelper.d("s getCount  i : " , i );
                    fragmentManager.beginTransaction().remove(mFragment).commit();
                    // this will clear the back stack and displays no animation on the screen
                    fragmentManager.popBackStackImmediate(mFragment.getTag(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getItemPosition(mFragment.getView());
                }
            }
        }
        notifyDataSetChanged();
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new SelectGroupFragment();
        } else if (position == 1){
            return new SelectGroupFragment();
        } else if (position == 2){
            return new SelectGroupFragment();
        } else {
            return new SelectGroupFragment();
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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
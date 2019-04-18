package com.buel.holyhelper.view.viewPage;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.buel.holyhelper.view.viewPage.adminPageViewFragment.SelectFragmentPagerAdapter;
import com.buel.holyhelper.view.viewPage.briefingPageViewFragment.BriefingFragmentPagerAdapter;
import com.buel.holyhelper.view.viewPage.mainPageViewFragment.MainFragmentPagerAdapter;

/**
 * CreateViewPageAdater
 *
 * onCreateFragmentAdapter 를 호출하여 필요한 Fragment를 세팅한다.
 * MainFragmentPagerAdapter 를 추가하여 view page를 다양하게 만들수 있다.
 * Created by blue7 on 2018-05-14.
 */

public class CreateViewPageAdater {

   public static FragmentPagerAdapter onCreateFragmentAdapter (Context context, FragmentManager fm , int num){
        if (num == 0) {
            //admin page fragment
            return new MainFragmentPagerAdapter(context, fm);
        } else if (num == 1){
            return new BriefingFragmentPagerAdapter(context, fm);
        }else{
             return new SelectFragmentPagerAdapter(context, fm);
        }
    }
}

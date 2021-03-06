package com.buel.holyhelpers.view.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentPagerAdapter;


import androidx.viewpager.widget.ViewPager;

import com.buel.holyhelpers.R;
import com.buel.holyhelpers.view.viewPage.CreateViewPageAdater;
import com.google.android.material.tabs.TabLayout;

public class SetCorpsActivity extends BaseActivity {

    private static final String FRAGMENT_TAG_OPTIONS_MENU = "options menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_corps);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.mainActivity_viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        //FragmentPagerAdapter adapter = CreateViewPageAdater.onCreateFragmentAdapter(this, getSupportFragmentManager() , 0);
        FragmentPagerAdapter adapter = CreateViewPageAdater.onCreateFragmentAdapter(this, getSupportFragmentManager() , 0);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.mainActivity_tablayout);
        tabLayout.setupWithViewPager(viewPager);

        adapter.notifyDataSetChanged();
    }
}

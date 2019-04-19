package com.buel.holyhelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.view.MainViewPager;
import com.buel.holyhelper.view.SimpleListener;
import com.commonLib.Common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.logger.LoggerHelper;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class BriefingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_briefing);
        CommonData.setViewMode(ViewMode.BRIEFING);

        FDDatabaseHelper.INSTANCE.getMyCorps(new SimpleListener.OnCompleteListener() {
            @Override
            public void onComplete() {
                setLayout();
            }
        });
    }
    /**
     * Main에 필요한 Layout을 그린다.
     */
    private void setLayout() {
        LoggerHelper.i("setLayout");
        CommonData.setViewMode(ViewMode.BRIEFING);

        TextView title = findViewById(R.id.member_manager_view_main_tv_desc);
        title.setText("BRIEFING");
        findViewById(R.id.member_activity_view_btn_no)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goMain();
                    }
                });

        setFloatingActionButton(BriefingActivity.this);
        setBriefingView();
    }


    /**
     * floating
     *
     * @param context
     */
    private void setFloatingActionButton(Context context) {
        View view = Common.getRootView(context);
        FloatingActionButton fab = view.findViewById(R.id.switch_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainViewPager.setDataAndTime(s -> {
                    if (s.equals("onComplete")) {
                        FDDatabaseHelper.INSTANCE.getAttend(() -> {});
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 메인 화면
     */
    private void setBriefingView() {
        MainViewPager.setMainViewPage(BriefingActivity.this, 1);
    }


}

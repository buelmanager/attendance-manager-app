package com.buel.holyhelpers.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.View;

import com.buel.holyhelpers.R;
import com.buel.holyhelpers.data.CommonData;
import com.buel.holyhelpers.data.CommonString;
import com.buel.holyhelpers.utils.SortMapUtil;
import com.commonLib.Common;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class CreateToolbarView {

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    private Toolbar toolbar;
    private Context context;

    @SuppressLint("ResourceAsColor")
    public Toolbar setToolbar(Context context) {

        this.context = context;
        AppCompatActivity appCompatActivity = (AppCompatActivity) context;
        View view = Common.getRootView(context);
        toolbar = view.findViewById(R.id.toolbar);
        appCompatActivity.setSupportActionBar(toolbar);

        DrawerLayout drawer = view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                appCompatActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        appCompatActivity.getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(appCompatActivity.getResources().getColor(R.color.colorToolbar)));

        return toolbar;
    }

    public void setToolbarText() {

        String teamName;
        if (CommonData.getGroupModel() == null || CommonData.getTeamModel() == null) {
            toolbar.setTitle(Html.fromHtml("<font color='red'><Strong>[ " + CommonString.INFO_TITLE_CONTROL_TEAM_OR_GROUP + " ]</Strong></font>"));
        } else {
            if (CommonData.getTeamModel().etc == null)
                teamName = "";
            else
                teamName = " : " + CommonData.getTeamModel().etc;

            toolbar.setTitle(Html.fromHtml("<Strong>[ " +
                            CommonData.getGroupModel().name + " ] </Strong>  /  [ " +
                            SortMapUtil.getInteger(CommonData.getTeamModel().name) + teamName + " ]"
                    )
            );
        }
    }
}

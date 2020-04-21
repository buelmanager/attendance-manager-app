package com.buel.holyhelpers.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import com.buel.holyhelpers.data.CommonData;
import com.buel.holyhelpers.model.TutorialModel;
import com.buel.holyhelpers.utils.AppUtil;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;
import com.orhanobut.logger.LoggerHelper;

import java.util.List;

public class TutorialViewActivity extends TutorialActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //fullscreen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setTutorialPage();
    }

    public void setTutorialPage() {
        if (CommonData.tutorialModelList == null) return;
        List<TutorialModel> drawableList = (List<TutorialModel>) CommonData.INSTANCE.tutorialModelList;

        int color = AppUtil.getRandomMaterialColor();
        for (int i = 0; i < drawableList.size(); i++) {
            TutorialModel tutorialModel = (drawableList.get(i));
            LoggerHelper.d(tutorialModel.toString());
            addFragment(new Step.Builder().setTitle(tutorialModel.title)
                    .setContent(tutorialModel.content)
                    //.setBackgroundColor(Color.parseColor(getString(tutorialModel.color))) // int background color
                    .setBackgroundColor(Color.parseColor(getString(color))) // int background color
                    .setDrawable(tutorialModel.drawable) // int top drawable
                    .build());
        }
    }

    @Override
    public void finishTutorial() {
        finish();
    }

    @Override
    public void currentFragmentPosition(int i) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

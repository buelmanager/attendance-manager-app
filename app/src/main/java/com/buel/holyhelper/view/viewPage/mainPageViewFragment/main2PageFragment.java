package com.buel.holyhelper.view.viewPage.mainPageViewFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buel.holyhelper.R;
import com.orhanobut.logger.LoggerHelper;

import androidx.fragment.app.Fragment;

public class main2PageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LoggerHelper.d("onCreateView" , "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        setPage(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        LoggerHelper.d("onResume" , "onResume");
        super.onResume();
    }


    @SuppressLint("LongLogTag")
    private void setPage(View rootView) {


    }
}

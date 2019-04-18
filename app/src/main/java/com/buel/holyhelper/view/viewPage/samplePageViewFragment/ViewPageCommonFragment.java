package com.buel.holyhelper.view.viewPage.samplePageViewFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.annotation.Nullable;

import androidx.fragment.app.Fragment;

/**
 * viewpage에 들어가는 common fragment
 * Created by blue7 on 2018-05-14.
 */

public class ViewPageCommonFragment extends Fragment {

    private String res;

    public ViewPageCommonFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ViewPageCommonFragment(String res) {
        this.res = res;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(Integer.parseInt(this.res), container, false);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
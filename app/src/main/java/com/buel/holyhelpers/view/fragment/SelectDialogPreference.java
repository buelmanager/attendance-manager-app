package com.buel.holyhelpers.view.fragment;

import android.content.Context;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.prefs.MaterialDialogPreference;

public class SelectDialogPreference extends MaterialDialogPreference {
    public SelectDialogPreference(Context context) {
        super(context);
    }

    public SelectDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectDialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SelectDialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}

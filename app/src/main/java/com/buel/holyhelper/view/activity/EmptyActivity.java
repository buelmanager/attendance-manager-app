package com.buel.holyhelper.view.activity;

import android.os.Bundle;
import android.view.View;

import com.buel.holyhelper.R;

public class EmptyActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        super.setTopTitleDesc("");

        setTopLayout(this);
    }

    @Override
    public void setTopLayout(View.OnClickListener onClickListener) {
        findViewById(R.id.top_bar_btn_ok).setVisibility(View.INVISIBLE);

        super.setTopLayout( onClickListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_bar_btn_back:
                goBackHistory();
                break;
            default:
                break;
        }
    }
}

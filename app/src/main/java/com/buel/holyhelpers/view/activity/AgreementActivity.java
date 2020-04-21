package com.buel.holyhelpers.view.activity;

import android.os.Bundle;
import android.view.View;

import com.buel.holyhelpers.R;
import com.buel.holyhelpers.data.CommonData;
import com.buel.holyhelpers.data.CommonString;
import com.commonLib.MaterialDailogUtil;

public class AgreementActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        findViewById(R.id.checkbox1_agreement).setOnClickListener(this);
        findViewById(R.id.detailview1_tv_agreement).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
         if(v.getId() == R.id.detailview1_tv_agreement){
             MaterialDailogUtil.Companion.simpleDoneDialog(AgreementActivity.this, "개인정보 수집/이용약관 및 소식알림 동의", CommonString.PERSON_DATA_CONTROL_INFO, new MaterialDailogUtil.OnDialogSelectListner() {
                 @Override
                 public void onSelect(String s) {

                 }
             });
         }else if(v.getId() == R.id.checkbox1_agreement){
             MaterialDailogUtil.Companion.simpleYesNoDialog(AgreementActivity.this, "서비스 이용약관에 동의하십니까?", new MaterialDailogUtil.OnDialogSelectListner() {
                 @Override
                 public void onSelect(String s) {
                     goJoin();
                     CommonData.historyClass = (Class)LoginActivity.class;
                 }
             });
        }
    }
}

package com.buel.holyhelper.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.data.UserType;
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.management.CorpsManager;
import com.buel.holyhelper.management.Management;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.model.UserModel;
import com.buel.holyhelper.view.SimpleListener;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.commonLib.SuperToastUtil;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.logger.LoggerHelper;

public class CorpsManagerViewActivity
        extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CorpsManagerViewActivity";

    EditText etName;
    EditText etOwner;
    EditText etPhone;
    EditText etAddress;
    EditText etDetailAddress;
    EditText etPassword;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corps_manager_view);


//Intent i = new Intent(MemberManagerViewActivity.this, DaumWebViewActivity.class);
//                startActivityForResult(i, DAUM_SEARCH_RESULT);
        etPassword = findViewById(R.id.corps_manager_activity_view_et_password);
        tvTitle = findViewById(R.id.recycler_view_main_tv_desc);
        etName = findViewById(R.id.corps_manager_activity_view_et_name);
        etOwner = findViewById(R.id.corps_manager_activity_view_et_owner);
        etPhone = findViewById(R.id.corps_manager_activity_view_et_phone);
        etAddress = findViewById(R.id.corps_manager_activity_view_et_address);
        etDetailAddress = findViewById(R.id.corps_manager_activity_view_et_detail_address);

        setFocusEditText(etName);
        setFocusEditText(etOwner);
        setFocusEditText(etPhone);
        setFocusEditText(etAddress);
        setFocusEditText(etDetailAddress);

        etAddress.setOnClickListener(this);
        setView();
        super.setTopLayout(this);
        super.setTopTitleDesc("교회/단체 정보 편집");
    }

    private void setView() {
        if (CommonData.getHolyModel() == null) return;

        HolyModel holyModel = CommonData.getHolyModel();
        etName.setText(holyModel.name);
        etOwner.setText(holyModel.owner);
        etPhone.setText(holyModel.phone);
        etAddress.setText(holyModel.address);
        if(holyModel.addressDetail==null) holyModel.addressDetail="";
        etDetailAddress.setText(holyModel.addressDetail);
        etPassword.setText(holyModel.password);
    }

    @SuppressLint("LongLogTag")
    private void sendServer() {

        HolyModel holyModel;

        if (CommonData.getCorpsCnt() >= 1) {
            holyModel = CommonData.getHolyModel();
        } else {
            holyModel = new HolyModel();
        }
        holyModel.adminUid = CommonData.getAdminUid();

        if(holyModel.adminUid ==null || holyModel.adminUid == ""){
            goLogout();
            return;
        }

        CorpsManager corpsManager = new CorpsManager();
        holyModel.address = etAddress.getText().toString();
        holyModel.imgUrl = "2222 1";
        holyModel.name = etName.getText().toString();
        holyModel.owner = etOwner.getText().toString();
        holyModel.phone = etPhone.getText().toString();
        holyModel.password = etPassword.getText().toString();
        holyModel.addressDetail = etDetailAddress.getText().toString();

        if (CommonData.getUserModel() != null) {

            UserModel userModel = CommonData.getUserModel();
            String userEmail = "";
            String adminName = "";
            String adminPhone = "";
            if (userModel.userEmail != null) userEmail = userModel.userEmail;
            if (userModel.userName != null) adminName = userModel.userName;
            if (userModel.userTell != null) adminPhone = userModel.userTell;

            holyModel.adminEmail = userEmail;
            holyModel.adminName = adminName;
            holyModel.adminPhone = adminPhone;
        }

        if (etName.getText().toString().equals("")) {
            SuperToastUtil.toast(this, "이름을 입력해주세요.");
            return;
        }
        if (etOwner.getText().toString().equals("")) {
            SuperToastUtil.toast(this, "담임목사를 입력해주세요.");
            return;
        }
        if (etPhone.getText().toString().equals("")) {
            SuperToastUtil.toast(this, "전화번호를 입력해주세요.");
            return;
        }
        if (etAddress.getText().toString().equals("")) {
            SuperToastUtil.toast(this, "주소를 입력해주세요.");
            return;
        }

        if (CommonData.getCorpsCnt() >= 1) {
            holyModel.uid = CommonData.getCorpsUid();
            corpsManager.modify(holyModel, new Management.OnCompleteListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(Object data) {
                    Log.d(TAG, "onComplete: Management.OnCompleteListener modify complete!!!");
                    FDDatabaseHelper.INSTANCE.getMyCorps(new SimpleListener.OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            goSelect();
                        }
                    });
                }
            });
        } else {

            LoggerHelper.d( holyModel.toString());
            holyModel.point = 0;
            holyModel.cumulativePoint = 0;
            holyModel.uid = holyModel.adminUid;
            corpsManager.insert(holyModel, new Management.OnCompleteListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(Object data) {
                    Log.d(TAG, "onComplete: Management.OnCompleteListener insert complete!!!");
                    FDDatabaseHelper.INSTANCE.getMyCorps(new SimpleListener.OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            if (CommonData.isTutoMode()) {
                                FDDatabaseHelper.INSTANCE.getMyCorps(new SimpleListener.OnCompleteListener() {
                                    @Override
                                    public void onComplete() {
                                        MaterialDailogUtil.simpleDoneDialog(CorpsManagerViewActivity.this,
                                                "#2 단계, 부서설정을 진행합니다.", new MaterialDailogUtil.OnDialogSelectListner() {
                                                    @Override
                                                    public void onSelect(String s) {
                                                        CommonData.setViewMode(ViewMode.ADMIN);
                                                        goSetGroup();
                                                    }
                                                });
                                    }
                                });
                            }
                            else {
                                goSelect();
                            }

                            HolyModel holyModel = new HolyModel();
                            holyModel.name = CommonData.getHolyModel().name;
                            holyModel.adminUid = CommonData.getHolyModel().adminUid;
                            holyModel.adminName = CommonData.getHolyModel().adminName;
                            holyModel.address = CommonData.getHolyModel().address;
                            holyModel.adminEmail = CommonData.getHolyModel().adminEmail;
                            holyModel.addressDetail = CommonData.getHolyModel().addressDetail;
                            holyModel.owner = CommonData.getHolyModel().owner;
                            holyModel.phone = CommonData.getHolyModel().phone;
                            holyModel.uid = CommonData.getHolyModel().uid;

                            FirebaseFirestore.getInstance().collection(FDDatabaseHelper.INSTANCE.getATTEND_TABLE())
                                    .document(FDDatabaseHelper.INSTANCE.getATTEND())
                                    .collection(FDDatabaseHelper.INSTANCE.getCORPS_TABLE())
                                    .document(CommonData.getHolyModel().uid)
                                    .set(holyModel);

                            FirebaseFirestore.getInstance().collection(FDDatabaseHelper.INSTANCE.getCORPS_TABLE())
                                    .document(CommonData.getHolyModel().uid)
                                    .set(holyModel);
                        }
                    });
                }
            });
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_bar_btn_ok:

                if (CommonData.getMemberShipType() != UserType.SUPER_ADMIN) {
                    String title = "권한이 없습니다.";
                    String ment = CommonData.getUserModel().userType + " 유저는 해당 권한이없습니다. 관리자에게 문의하세요.";
                    MaterialDailogUtil.simpleDoneDialog(CorpsManagerViewActivity.this, title, ment, new MaterialDailogUtil.OnDialogSelectListner() {
                        @Override
                        public void onSelect(String s) {
                            goSelect();
                        }
                    });
                    return;
                }

                Common.hideKeyboard(CorpsManagerViewActivity.this);
                sendServer();
                break;

            case R.id.corps_manager_activity_view_et_address:
                Intent i = new Intent(CorpsManagerViewActivity.this, DaumWebViewActivity.class);
                startActivityForResult(i, DaumWebViewActivity.DAUM_SEARCH_RESULT);
                break;
            case R.id.top_bar_btn_back:
                goBackHistory();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == DaumWebViewActivity.DAUM_SEARCH_RESULT) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                etAddress.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }
    }

}

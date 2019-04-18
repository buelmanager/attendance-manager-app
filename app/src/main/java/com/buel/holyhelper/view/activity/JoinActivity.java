package com.buel.holyhelper.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.AdminMode;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.data.TutorialViewerUtil;
import com.buel.holyhelper.data.UserType;
import com.buel.holyhelper.management.FireStoreManager;
import com.buel.holyhelper.management.PointManager;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.model.UserModel;
import com.buel.holyhelper.view.DataReturnListener;
import com.buel.holyhelper.view.DataTypeListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.commonLib.PatternDefine;
import com.commonLib.SuperToastUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.orhanobut.logger.LoggerHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;

public class JoinActivity extends BaseActivity implements View.OnClickListener {

    private static final int PIC_FROM_ALBUM = 10;

    private EditText editText_name;
    private EditText editText_tell;
    private EditText editText_uid;
    private ImageView imageView_profile;
    private Uri mImageUri;
    private String adminUID = null;
    private String corpsName = null;
    private List<HolyModel> tempHolyModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        LoggerHelper.d("start JoinActivity");

        editText_uid = findViewById(R.id.joinActivity_edittext_corps_uid);
        editText_name = findViewById(R.id.joinActivity_edittext_name);
        editText_tell = findViewById(R.id.joinActivity_edittext_tell);
        imageView_profile = findViewById(R.id.joinActivity_imageivew_profile);
        imageView_profile.setOnClickListener(this);
        editText_uid.setOnClickListener(this);
        findViewById(R.id.helper_btn).setOnClickListener(this);

        setFocusEditText(editText_name);
        setFocusEditText(editText_tell);

        setTopLayout(this);
        setViewMode();
    }

    /**
     * ViewMode 에 맞게 화면을 구성한다.
     */
    private void setViewMode() {
        if (CommonData.getAdminMode() == AdminMode.MODIFY) {  //수정 모드
            setModifyViewMode();
        } else {                                            //가입 모드
            joinStep1();
        }
    }

    private void setJoinViewMode() {
        if (selectedUserType == UserType.SUB_ADMIN) {
            setTopDetailDesc(View.VISIBLE, "* 부 관리자는 운영관리와 제한적 권한을 가지고 있습니다.");
        } else {
            setTopDetailDesc(View.VISIBLE, "* 최고 관리자는 모든 권한을 가지고 있습니다. \n 각 교회마다 1계정만 존재합니다.");

            findViewById(R.id.joinActivity_textInputLayout_corps).setVisibility(View.INVISIBLE);
        }
        super.setTopTitleDesc("계정 등록");
    }

    private void setModifyViewMode() {
        HolyModel holyModel = CommonData.getHolyModel();
        UserModel userModel = CommonData.getUserModel();

        editText_name.setClickable(false);
        editText_name.setFocusableInTouchMode(false);
        editText_name.setText(userModel.userName);
        editText_tell.setText(userModel.userTell);

        corpsName = userModel.corpsName;
        adminUID = CommonData.getAdminUid();

        LoggerHelper.d("setModifyViewMode" , CommonData.getMemberShipType());

        if (holyModel.uid != null) editText_uid.setText(holyModel.name);

        super.setTopTitleDesc("계정 수정");
        setTopDetailDesc(View.VISIBLE, "* 계정 수정 모드입니다.");

        try {
            LoggerHelper.d("userModel.userPhotoUri :  " + userModel.userPhotoUri);
            Glide.with(JoinActivity.this)
                    .load(userModel.userPhotoUri)
                    .apply(new RequestOptions().circleCrop())
                    .into(imageView_profile);
        } catch (Exception e) {
            LoggerHelper.e(e.getMessage());
        }
    }

    private UserType selectedUserType;

    private void joinStep1() {
        super.setTopTitleDesc("계정 타입을 설정");

        MaterialDailogUtil.showSingleChoice(
                JoinActivity.this,
                "계정 타입을 설정해 주세요.",
                R.array.membership_option,
                new MaterialDailogUtil.OnDialogSelectListner() {
                    @Override
                    public void onSelect(String s) {
                        LoggerHelper.d("showSingleChoice  s : " + s);
                        if (s.equals("0")) { //super 관리자
                            selectedUserType = UserType.SUPER_ADMIN;
                        } else if (s.equals("1")) {// 부 관리자
                            selectedUserType = UserType.SUB_ADMIN;
                        }

                        String userTypeName = null;
                        if (selectedUserType.equals(UserType.SUPER_ADMIN))
                            userTypeName = "최고 관리자";
                        else if (selectedUserType.equals(UserType.SUB_ADMIN))
                            userTypeName = "운영 관리자";

                        List<String> okList = new ArrayList<>();
                        okList.add("동의합니다.");
                        okList.add("다시 선택하겠습니다.");

                        MaterialDailogUtil.showSingleChoice(
                                JoinActivity.this,
                                "[ " + userTypeName + " ] 계정으로 선택하셨습니다. \n동의하십니까?",
                                okList,
                                new MaterialDailogUtil.OnDialogSelectListner() {
                                    @Override
                                    public void onSelect(String s) {
                                        if (s.equals("0")) { //super 관리자
                                            popToast("계정선택이 완료되었습니다.");
                                            setJoinViewMode();
                                        } else if (s.equals("1")) {// 부 관리자
                                            joinStep1();
                                        }
                                    }
                                });

                    }
                });
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.helper_btn:
                TutorialViewerUtil.getCreateAccountTutorialModels(JoinActivity.this);
                break;
            //교회선택하기 클릭
            case R.id.joinActivity_edittext_corps_uid:
                onClickedSetSerial();
                break;

            //돌아가기 버튼 클릭
            case R.id.top_bar_btn_back:
                /*if (CommonData.getViewMode() == ViewMode.MODIFY ||
                        CommonData.getViewMode() == ViewMode.ADD_ACOUNT_SUB_ADMIN) {
                    goSettings();
                } else {
                    goLogin();
                } */
                goBackHistoryIntent();
                break;

            //저장버튼 클릭했을때
            case R.id.top_bar_btn_ok:

                //수정하기 모드일때 할때
                if (CommonData.getAdminMode() == AdminMode.MODIFY) {
                    onOkclickedModify();
                } else {
                    onJoinClick(v);
                }
                break;

            //사진 등록하기 버튼 클릭
            case R.id.joinActivity_imageivew_profile:
                onProfileClick();
                break;
        }
    }



    private boolean isClickedSetSerial = false;
    private void onClickedSetSerial() {
        /*if (CommonData.getViewMode() == ViewMode.MODIFY || CommonData.getViewMode() == ViewMode.ADD_ACOUNT_SUB_ADMIN) {
            SuperToastUtil.toastE(JoinActivity.this, "변경할수 없습니다.");
            return;
        }
*/
        if (selectedUserType == UserType.SUPER_ADMIN || CommonData.getMemberShipType() == UserType.SUPER_ADMIN) {
            SuperToastUtil.toastE(JoinActivity.this, "SUPER_ADMIN 유저입니다.");
            return;
        }

        LoggerHelper.d ("onClickedSetSerial" , "CommonData.getAdminMode() : " + CommonData.getAdminMode());
        LoggerHelper.d ("onClickedSetSerial" , "isClickedSetSerial : " + isClickedSetSerial);
        if (CommonData.getAdminMode() == AdminMode.MODIFY && !isClickedSetSerial && CommonData.getMemberShipType() != UserType.SUPER_ADMIN) {
            MaterialDailogUtil.simpleYesNoDialog(JoinActivity.this, CommonString.CORP_NICK + " 을/를 선택시 [운영 관리자] 권한을 다시 요청해야 합니다. 실행하시겠습니까?", new MaterialDailogUtil.OnDialogSelectListner() {
                @Override
                public void onSelect(String s) {
                    isClickedSetSerial =true;
                    onClickedSetSerial();
                }
            });
            return;
        }

        MaterialDailogUtil.simpleInputDoneDialog(JoinActivity.this, "교회 찾기!", "교회 이름만 넣어도 됩니다.", new MaterialDailogUtil.OnDialogSelectListner() {
            @Override
            public void onSelect(String s) {

                s = s.replaceAll(PatternDefine.PATTERN_BLANK, "");

                CommonData.setStrSearch(s);

                LoggerHelper.e("MaterialDailogUtil.simpleInputDoneDialog onSelect");
                FDDatabaseHelper.INSTANCE.getAllCorpsData(new FDDatabaseHelper.onFDDCallbackListener() {
                    @Override
                    public void onFromDataComplete(int DataCode, DataSnapshot dataSnapshot) {
                        LoggerHelper.d(CommonData.getHolyModelList());

                        List<HolyModel> holyModels = CommonData.getHolyModelList();
                        List<String> tempList = new ArrayList<>();
                        tempHolyModelList = new ArrayList<>();
                        LoggerHelper.d("JoinActivity", "holyModels : " + holyModels.size());

                        Integer c = 0;
                        for (HolyModel elem : holyModels) {

                            LoggerHelper.d("JoinActivity", "CommonData.getStrSearch() : " + CommonData.getStrSearch());
                            LoggerHelper.d("JoinActivity", "elem.name : " + elem.name);

                            if (elem.name != null) {
                                int compareMemberName = elem.name.indexOf(CommonData.getStrSearch());
                                if (compareMemberName != -1) {
                                    LoggerHelper.d("JoinActivity", "단체 추가합니다. => " + elem.name);
                                    tempList.add(c, "교회이름: " + elem.name + "\n담임목사 : " + elem.owner + "\n교회주소 : " + elem.address + "\n교회전화 : " + elem.phone);
                                    tempHolyModelList.add(elem);
                                    c++;
                                }
                            }
                        }

                        MaterialDailogUtil.showSingleChoice(JoinActivity.this, CommonString.CORP_NICK + " 을/를 선택하세요.", tempList, new MaterialDailogUtil.OnDialogSelectListner() {
                            @Override
                            public void onSelect(String s) {

                                try {
                                    LoggerHelper.d("s : " + s);
                                    LoggerHelper.d("s : " + tempHolyModelList.get(Integer.parseInt(s)));
                                    HolyModel holyModel = tempHolyModelList.get(Integer.parseInt(s));
                                    LoggerHelper.d(holyModel.convertMap());

                                    editText_uid.setText(holyModel.name);
                                    adminUID = holyModel.adminUid;
                                    corpsName = holyModel.name;

                                } catch (Exception e) {
                                    LoggerHelper.d(e.getMessage());
                                    LoggerHelper.d("선택을 하지 않은 경우");
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 저장하기 버튼 클릭 : 수정하기 모드일 때
     */
    private void onOkclickedModify() {
        FirebaseUser user = CommonData.getFirebaseUser();
        UserModel userModel = CommonData.getUserModel();
        userModel.userName = editText_name.getText().toString();
        userModel.userTell = editText_tell.getText().toString();

        if (UserType.SUB_ADMIN == CommonData.getMemberShipType()) {
            if (corpsName == null || adminUID == null) {
                sToast(CommonString.INFO_TITLE_SELECT_CORP, true);
                return;
            }
            userModel.corpsName = corpsName;
        }

        //isClickedSetSerial : true 이면 , 1. SUPER_ADMIN 이 아니고 2. 교회 선택하기를 클릭한 상태이다.
        if(isClickedSetSerial){
            userModel.permission = "no";
        }
        userModel.userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        userModel.userType = CommonData.getUserModel().userType;

        LoggerHelper.e(userModel.toString());
        if (mBitmap != null) {
            FDDatabaseHelper.INSTANCE.setUploadImageSendUserStorage(userModel, mBitmap, new DataReturnListener.OnCompleteListener() {
                @Override
                public void onComplete(String str) {
                    userModel.userPhotoUri = str;

                    try {

                        FDDatabaseHelper.INSTANCE.sendUserDataInsertUserModel(user.getUid(), userModel, aBoolean -> {
                            if (UserType.SUPER_ADMIN != CommonData.getMemberShipType())
                                CommonData.setAdminUid(userModel.adminUID);
                            goMain();
                        });
                    } catch (Exception e) {
                        SuperToastUtil.toast(JoinActivity.this, e.getMessage());
                    }
                }
            });
        } else {
            try {

                FDDatabaseHelper.INSTANCE.sendUserDataInsertUserModel(user.getUid(), userModel, aBoolean -> {
                    if (UserType.SUPER_ADMIN != CommonData.getMemberShipType())
                        CommonData.setAdminUid(userModel.adminUID);
                    goMain();
                });
            } catch (Exception e) {
                SuperToastUtil.toast(JoinActivity.this, e.getMessage());
            }
        }
    }

    String nameStr;
    String tellStr;

    /**
     * 저장하기 버튼을 클릭하였을 때
     * <p>
     * 1. 구조 확인 ( 공란, 이메일, 패스워드 ..)
     * 2. check macadress : 서버의 전체 유저의 mac adress 확인
     * 3. firebase create user
     *
     * @param v
     */
    private void onJoinClick(View v) {

        nameStr = editText_name.getText().toString();
        tellStr = editText_tell.getText().toString();

        //이미지가 없다면 기본 이미지를 쓴다.
        if (mImageUri == null) {
            mImageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.man);
        }

        setUserDataOnFireBase();
    }

    private void setUserDataOnFireBase() {
        UserModel userModel = new UserModel();
        if (nameStr.length() <= 0 || tellStr.length() <= 0) {
            sToast("공란이 있습니다.", true);
            return;
        }

        try {
            userModel.userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        } catch (Exception e) {
            popToast("설정하신 계정의 이메일이 없습니다. 관리자에게 문의하세요.");
            userModel.userEmail = "";
        }
        userModel.userName = nameStr;
        userModel.userTell = tellStr;
        userModel.macAddress = Common.getMacAdress();
        userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (UserType.SUPER_ADMIN == selectedUserType) {
            userModel.permission = "ok";
            userModel.adminUID = FirebaseAuth.getInstance().getUid();
            CommonData.setAdminUid(userModel.adminUID);
        }else if (UserType.SUB_ADMIN == selectedUserType) {
            if (corpsName == null || adminUID == null) {
                sToast(CommonString.INFO_TITLE_SELECT_CORP, true);
                return;
            }

            userModel.corpsName = corpsName;
            userModel.permission = "no";
            userModel.adminUID = adminUID;

        }

        userModel.userType = selectedUserType.toString();

        LoggerHelper.d("mImageUri : " + mImageUri);
        LoggerHelper.d(userModel.toString());

        if (mBitmap == null) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            mImageUri = firebaseAuth.getCurrentUser().getPhotoUrl();
            userModel.userPhotoUri = mImageUri.toString();
            popToast("프로필사진으로 대처합니다.");
            updateUserTable(userModel.uid, userModel);
            return;
        }

        FDDatabaseHelper.INSTANCE.setUploadImageSendUserStorage(userModel, mBitmap, new DataReturnListener.OnCompleteListener() {
            @Override
            public void onComplete(String str) {
                userModel.userPhotoUri = str;
                updateUserTable(userModel.uid, userModel);
            }
        });
    }

    private void updateUserTable(String uid, UserModel userModel) {

        LoggerHelper.d("MACADRESS", userModel.macAddress);
        //progressDialog.show();
        try {
            FireStoreManager.sendUserDataInsertUserModel(userModel, new DataTypeListener.OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(Boolean aBoolean) {
                    CommonData.setMemberShipType(selectedUserType);
                    CommonData.setUserModel(userModel);
                    if (selectedUserType == UserType.SUB_ADMIN) {
                        CommonData.setAdminUid(userModel.adminUID);
                        PointManager.setPlusPoint(CommonData.getPersonalJoinPoint());
                        Toast.makeText(JoinActivity.this, "회원가입이 완료되었습니다. '가입 포인트' " + CommonData.getPersonalJoinPoint() + " 지급됩니다.", Toast.LENGTH_SHORT).show();
                        goMain();
                    } else if (selectedUserType == UserType.SUPER_ADMIN) {
                        CommonData.setIsTutoMode(true);
                        goMain();
                    }
                }
            });
        } catch (Exception e) {
            SuperToastUtil.toast(JoinActivity.this, e.getMessage());
            LoggerHelper.e(e.getMessage());
        }
    }

    private void onProfileClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PIC_FROM_ALBUM);
    }

    @SuppressLint("MissingPermission")
    private String getPhone() {

        String wantPermission = Manifest.permission.READ_PHONE_STATE;

        TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(JoinActivity.this, wantPermission) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return phoneMgr.getLine1Number();
    }

    private Bitmap mBitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PIC_FROM_ALBUM && requestCode == RESULT_OK) ;
        {
            if (data == null) return;
            mImageUri = data.getData();
            imageView_profile.setImageURI(mImageUri);
            ImageView imgView = imageView_profile;

            try {
                mBitmap = Common.getBitmapFromUri(JoinActivity.this, mImageUri);
                imgView.setImageBitmap(mBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
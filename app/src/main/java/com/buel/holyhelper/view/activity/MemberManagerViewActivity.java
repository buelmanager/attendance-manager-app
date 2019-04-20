package com.buel.holyhelper.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.AdminMode;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.management.Management;
import com.buel.holyhelper.management.MemberManager;
import com.buel.holyhelper.model.HolyModel;
import com.buel.holyhelper.utils.SortMapUtil;
import com.buel.holyhelper.view.DataReturnListener;
import com.buel.holyhelper.view.SimpleListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.commonLib.SuperToastUtil;
import com.orhanobut.logger.LoggerHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class MemberManagerViewActivity extends BaseActivity implements View.OnClickListener {
    private static final int PIC_FROM_ALBUM = 10;
    private static final String TAG = "MemberManagerViewActivity";
    private static final int DAUM_SEARCH_RESULT = 101;
    EditText etName;
    EditText etPhone;
    EditText etAdd;
    EditText etAdd2;
    EditText etGender;
    EditText etLeader;
    EditText etPosition;
    EditText etGroup;
    EditText etTeam;
    EditText etNew;
    EditText etIsExecutives;
    EditText etBirth;
    private ImageView imageView_profile;
    private Uri mImageUri;
    private String oldImageUri;
    private String mTell;

    private Bitmap mBitmap;

    HolyModel.groupModel selectGroup;
    HolyModel.groupModel.teamModel selectTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memeber_view);

        TextView titleView = findViewById(R.id.member_manager_view_main_tv_desc);

        if (CommonData.getHolyModel() == null) {
            Toast.makeText(this, CommonString.INFO_TITLE_CONTROL_CORP, Toast.LENGTH_SHORT).show();
            goSelect();
            return;
        }

        //그룹 리스트가 없는경우, 그룹선택이 안되어 있는경우
        if (CommonData.getHolyModel().group == null || CommonData.getGroupModel() == null) {
            Toast.makeText(this, CommonString.INFO_TITLE_CONTROL_GROUP, Toast.LENGTH_SHORT).show();
            goSelect();
            return;
        }

        etGroup = findViewById(R.id.member_activity_view_et_group);
        etTeam = findViewById(R.id.member_activity_view_et_team);
        etName = findViewById(R.id.member_activity_view_et_name);
        etPhone = findViewById(R.id.member_activity_view_et_phone);
        etAdd = findViewById(R.id.member_activity_view_et_add);
        etAdd2 = findViewById(R.id.member_activity_view_et_add2);
        etGender = findViewById(R.id.member_activity_view_et_gender);
        etLeader = findViewById(R.id.member_activity_view_et_leader);
        etPosition = findViewById(R.id.member_activity_view_et_position);
        etNew = findViewById(R.id.member_activity_view_et_new);
        etIsExecutives = findViewById(R.id.member_activity_view_et_isexecutives);
        etBirth = findViewById(R.id.member_activity_view_et_birth);
        imageView_profile = findViewById(R.id.member_activity_view_imageivew_profile);
        imageView_profile.setOnClickListener(this);
        etAdd.setOnClickListener(this);
        //etAdd2.setVisibility(View.INVISIBLE);

        Button btnGroup = findViewById(R.id.member_activity_view_btn_group);
        Button btnTeam = findViewById(R.id.member_activity_view_btn_team);
        Button btnGender = findViewById(R.id.member_activity_view_btn_gender);
        Button btnPosition = findViewById(R.id.member_activity_view_btn_position);
        Button btnNew = findViewById(R.id.member_activity_view_btn_new);
        Button btnIsExcutive = findViewById(R.id.member_activity_view_btn_isexecutives);
        Button btnBirth = findViewById(R.id.member_activity_view_btn_birth);
        Button btnCall = findViewById(R.id.member_activity_view_btn_call);

        //AppUtil.setBackColor(MemberManagerViewActivity.this, btnCall, R.color.vordiplom_color_green);

        setFocusEditText(etName);
        setFocusEditText(etPhone);
        setFocusEditText(etAdd);
        setFocusEditText(etGender);
        setFocusEditText(etLeader);
        setFocusEditText(etPosition);
        setFocusEditText(etNew);
        setFocusEditText(etIsExecutives);
        setFocusEditText(etBirth);

        CommonData.setSelectedGroup(CommonData.getGroupModel());
        CommonData.setSelectedTeam(CommonData.getTeamModel());

        if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            HolyModel.memberModel member = CommonData.getSelectedMember();

            etName.setText(member.name);
            etPhone.setText(member.phone);
            etAdd.setText(member.address);
            etAdd2.setText(member.town);
            etGender.setText(member.gender);
            etLeader.setText(member.leader);
            etPosition.setText(member.position);
            etGroup.setText(CommonData.getGroupName(member.groupUID));

            try {
                etTeam.setText(SortMapUtil.getInteger(CommonData.getTeamName(member.teamUID)).toString());
            } catch (Exception e) {
                etTeam.setText(member.teamName);
            }

            if (!member.groupUID.equals(CommonData.getGroupModel().uid)) {
                HolyModel.groupModel group = FDDatabaseHelper.INSTANCE.getGroupModelFromMap(CommonData.getHolyModel().group, String.valueOf(member.groupUID));

                if (group.name == null) {
                    popToast("해당부서가 없습니다. 같은 이름으로 생성해주세요.");
                    goSelect();
                    return;
                }

                CommonData.setSelectedGroup(group);

                if (member.teamName != null && member.teamUID != null)
                    etTeam.setText(member.teamName);

                popToast(group.name + " 으로 부서를 설정하였습니다.");

            }

            etNew.setText(member.isNew);
            etNew.setText(member.isNew);
            etIsExecutives.setText(member.isExecutives);
            etBirth.setText(member.birth);

            oldImageUri = member.userPhotoUri;

            if (member.userPhotoUri == null) {
                try {
                    Glide.with(MemberManagerViewActivity.this)
                            .load(R.drawable.ic_account)
                            .apply(new RequestOptions().circleCrop())
                            .into(imageView_profile);
                } catch (Exception e) {
                    LoggerHelper.e(e.getMessage());
                }
            } else {
                Glide.with(MemberManagerViewActivity.this)
                        .load(member.userPhotoUri)
                        .apply(new RequestOptions().circleCrop())
                        .into(imageView_profile);
            }
        } else if (CommonData.getAdminMode() == AdminMode.NORMAL) {

            etIsExecutives.setText("회원/성도");
            if (CommonData.getGroupModel() != null)
                etGroup.setText(CommonData.getGroupModel().name);


            if (CommonData.getTeamModel() != null) {
                LoggerHelper.d("CommonData.getTeamModel() : " + CommonData.getTeamModel());
                LoggerHelper.d("CommonData.getTeamModel() toString  : " + CommonData.getTeamModel().toString());
                etTeam.setText(SortMapUtil.getInteger(CommonData.getTeamModel().name).toString() + "  " + CommonData.getTeamModel().etc);
            }


            btnCall.setVisibility(View.INVISIBLE);
        }

        etName.setOnClickListener(this);
        etBirth.setOnClickListener(this);
        etGroup.setOnClickListener(this);
        etTeam.setOnClickListener(this);
        etGender.setOnClickListener(this);
        etPosition.setOnClickListener(this);
        etNew.setOnClickListener(this);
        etIsExecutives.setOnClickListener(this);
        btnCall.setOnClickListener(this);

        btnGroup.setOnClickListener(this);
        btnTeam.setOnClickListener(this);
        btnGender.setOnClickListener(this);
        btnPosition.setOnClickListener(this);
        btnBirth.setOnClickListener(this);
        btnNew.setOnClickListener(this);
        btnIsExcutive.setOnClickListener(this);

        setTopLayout(this);
        super.setTopTitleDesc("성도/회원 수정/관리 페이지");
    }

    boolean isFstMember = false;

    @SuppressLint("LongLogTag")
    private void sendMemberData() {
        MemberManager memberManager = new MemberManager();
        HolyModel.memberModel memberModel = new HolyModel.memberModel();
        ArrayList<HolyModel.memberModel> membersArrayList = getData();
        Boolean isCompare = getCompareData(etName.getText().toString(), membersArrayList);

        if (etName.getText().toString().equals("")) {
            SuperToastUtil.toast(this, "이름을 입력해주세요.");
            isSaving = false;
            return;
        }

        if (etNew.getText().toString().equals("")) {
            SuperToastUtil.toast(this, "등록구분을 입력해주세요.");
            isSaving = false;
            return;
        }

        if (etPhone.getText().toString().equals("") ||
                etGender.getText().toString().equals("") ||
                etLeader.getText().toString().equals("") ||
                etBirth.getText().toString().equals("")) {
            SuperToastUtil.toast(this, "전화/생년월일/성별/인도자를  입력해주세요.");
            isSaving = false;
            return;
        }

        if (CommonData.getSelectedGroup() == null || CommonData.getSelectedTeam() == null) {
            SuperToastUtil.toast(this, "선택된 " + CommonString.GROUP_NICK + " 또는 " + CommonString.TEAM_NICK + "이/가 없습니다.");
            isSaving = false;
            return;
        }

        if (isCompare) {
            Log.d(TAG, "sendServer: isCompare = " + isCompare);
            SuperToastUtil.toast(this, "같은 이름이 있습니다.");
            isSaving = false;
            return;
        }

        memberModel.name = etName.getText().toString();
        memberModel.phone = etPhone.getText().toString();
        memberModel.address = etAdd.getText().toString();
        memberModel.town = etAdd2.getText().toString();
        memberModel.gender = etGender.getText().toString();
        memberModel.leader = etLeader.getText().toString();
        memberModel.position = etPosition.getText().toString();
        memberModel.corpsUID = CommonData.getCorpsUid();
        memberModel.corpsName = CommonData.getHolyModel().name;

        memberModel.groupName = CommonData.getSelectedGroup().name;
        memberModel.teamName = CommonData.getSelectedTeam().name;
        memberModel.groupUID = CommonData.getSelectedGroup().uid;
        memberModel.teamUID = CommonData.getSelectedTeam().uid;
        memberModel.isNew = etNew.getText().toString();
        memberModel.isExecutives = etIsExecutives.getText().toString();
        memberModel.birth = etBirth.getText().toString();

        if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            if (mImageUri == null) {
                memberModel.userPhotoUri = oldImageUri;
            }
        }

        LoggerHelper.d("MemberManagerViewAcitivity", memberModel.toString());
        LoggerHelper.d("MemberManagerViewAcitivity", "CommonData.getViewMode() = " + CommonData.getViewMode());
        if (isFstMember) {
            memberModel.memberRegistDate = Common.currentTimestamp();
        }

        setSendToServer(memberManager, memberModel, new SimpleListener.OnCompleteListener() {
            @Override
            public void onComplete() {
                if (mImageUri == null) {
                    if (CommonData.isTutoMode()) {
                        MaterialDailogUtil.Companion.simpleDoneDialog(MemberManagerViewActivity.this,
                                "#5 단계, 축하합니다. 이제 출석체크하러 갑니다!", new MaterialDailogUtil.OnDialogSelectListner() {
                                    @Override
                                    public void onSelect(String s) {
                                        goMain();
                                    }
                                });
                    } else {
                        //CommonData.setViewMode(ViewMode.ADMIN);
                        //goMemberRecycler();
                        finish();
                        isSaving = false;
                    }
                    return;
                }

                showProgressDialog(true);
                FDDatabaseHelper.INSTANCE.setUploadImageSendMemberStorage(uploadmember, mBitmap, new DataReturnListener.OnCompleteListener() {
                    @Override
                    public void onComplete(String str) {
                        uploadmember.userPhotoUri = str;
                        memberManager.modify(uploadmember, new Management.OnCompleteListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onComplete(Object data) {
                                Log.d(TAG, "MODIFY onComplete: memberManager insert complete!!!");

                                FDDatabaseHelper.INSTANCE.getAllcorpsMembers(new SimpleListener.OnCompleteListener() {
                                    @Override
                                    public void onComplete() {
                                        showProgressDialog(false);

                                        if (CommonData.isTutoMode()) {
                                            MaterialDailogUtil.Companion.simpleDoneDialog(MemberManagerViewActivity.this,
                                                    "#5 단계, 축하합니다. 이제 출석체크하러 갑니다!", new MaterialDailogUtil.OnDialogSelectListner() {
                                                        @Override
                                                        public void onSelect(String s) {
                                                            goMain();
                                                        }
                                                    });
                                        } else {
                                            //CommonData.setViewMode(ViewMode.ADMIN);
                                            //goMemberRecycler();
                                            isSaving = false;
                                            finish();
                                        }

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private HolyModel.memberModel uploadmember = null;

    private void setSendToServer(MemberManager memberManager, HolyModel.memberModel memberModel, SimpleListener.OnCompleteListener onCompleteListener) {
        if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            memberModel.uid = CommonData.getSelectedMember().uid;
            memberManager.modify(memberModel, new Management.OnCompleteListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(Object data) {
                    Log.d(TAG, "MODIFY onComplete: memberManager insert complete!!!");

                    FDDatabaseHelper.INSTANCE.getAllcorpsMembers(new SimpleListener.OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            ArrayList<HolyModel.memberModel> membersArrayList = getData();
                            Boolean isCompare = getCompareData(etName.getText().toString(), membersArrayList);

                            uploadmember = getSerchMember(memberModel, membersArrayList);
                            onCompleteListener.onComplete();

                        }
                    });
                }
            });
        } else if (CommonData.getAdminMode() == AdminMode.NORMAL) {
            memberManager.insert(memberModel, new Management.OnCompleteListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onComplete(Object data) {
                    Log.d(TAG, "ADMIN onComplete: memberManager insert complete!!!");

                    FDDatabaseHelper.INSTANCE.getAllcorpsMembers(new SimpleListener.OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            ArrayList<HolyModel.memberModel> membersArrayList = getData();
                            Boolean isCompare = getCompareData(etName.getText().toString(), membersArrayList);

                            uploadmember = getSerchMember(memberModel, membersArrayList);
                            onCompleteListener.onComplete();

                        }
                    });
                }
            });
        }
    }

    /**
     * 멤버 리스트를 가지고 온다.
     *
     * @return
     */
    public ArrayList getData() {
        final ArrayList<HolyModel.memberModel> members = new ArrayList<>();
        try {
            LoggerHelper.d("CommonData.getMembersMap() size : +  "  + CommonData.getMembersMap().size());
            //for (Map.Entry<String, HolyModel.memberModel> elem : CommonData.getHolyModel().memberModel.entrySet()) {
            for (Map.Entry<String, HolyModel.memberModel> elem : CommonData.getMembersMap().entrySet()) {
                HolyModel.memberModel eleGroup = elem.getValue();
                eleGroup.uid = elem.getKey();
                members.add(eleGroup);
            }
        } catch (Exception e) {
        }
        return members;
    }

    /**
     * 데이터를 비교한다.
     *
     * @param strComare
     * @param members
     * @return
     */
    public boolean getCompareData(
            String strComare,
            ArrayList<HolyModel.memberModel> members) {

        if (CommonData.getAdminMode() == AdminMode.MODIFY && CommonData.getSelectedMember().name.equals(strComare)) {
            //if (CommonData.getSelectedGroup().name.equals(strComare)) {
            return false;
            //}
        }

        if (strComare.equals("")) {
            SuperToastUtil.toast(MemberManagerViewActivity.this, "이름을 입력해주세요.");
            return true;
        }

        //단체에서
        for (HolyModel.memberModel eleMember : members) {
            //그룹이 같고
            LoggerHelper.d(" eleMember.groupUID : " + eleMember.groupUID);
            LoggerHelper.d(" CommonData.getGroupModel().uid : " + CommonData.getGroupModel().uid);

            if (eleMember.groupUID != null) {
                if ((eleMember.groupUID).equals(CommonData.getGroupModel().uid)) {
                    //이름이 같은것을 검색
                    if ((eleMember.name).equals(strComare)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 데이터를 비교한다.
     * 현재 모델과 비교하여서 같은이름의  uid를 받아온다
     *
     * @param cMember
     * @param members
     * @return
     */
    public HolyModel.memberModel getSerchMember(
            HolyModel.memberModel cMember,
            ArrayList<HolyModel.memberModel> members) {
        HolyModel.memberModel searchMember = null;

        for (HolyModel.memberModel eleMember : members) {
            if ((eleMember.name.equals(cMember.name))
                    && (eleMember.groupUID.equals(cMember.groupUID))) {
                searchMember = eleMember;
                return searchMember;
            }
        }
        return searchMember;
    }

    private void onProfileClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PIC_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PIC_FROM_ALBUM && resultCode == RESULT_OK) {
            if (data == null) return;

            LoggerHelper.d("requestCode : " + requestCode);
            LoggerHelper.d("resultCode : " + resultCode);
            LoggerHelper.d("data : " + data.getData());

            mImageUri = data.getData();
            imageView_profile.setImageURI(mImageUri);
            ImageView imgView = imageView_profile;

            try {
                mBitmap = Common.getBitmapFromUri(MemberManagerViewActivity.this, mImageUri);
                imgView.setImageBitmap(mBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            LoggerHelper.d("이미지 추가");

        } else if (requestCode == DAUM_SEARCH_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                etAdd.setText(result);
                etAdd2.setVisibility(View.VISIBLE);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.member_activity_view_et_add:
                Intent i = new Intent(MemberManagerViewActivity.this, DaumWebViewActivity.class);
                startActivityForResult(i, DAUM_SEARCH_RESULT);
                break;
            case R.id.member_activity_view_et_position:
            case R.id.member_activity_view_btn_position:
                MaterialDailogUtil.Companion.simpleListDialog(
                        MemberManagerViewActivity.this,
                        R.array.position_option,
                        new MaterialDailogUtil.OnDialogSelectListner() {
                            @Override
                            public void onSelect(String s) {
                                etPosition.setText(MaterialDailogUtil.Companion.getPosition(Integer.valueOf(s)));
                            }
                        });
                break;


            case R.id.member_activity_view_btn_birth:
            case R.id.member_activity_view_et_birth:
                MaterialDailogUtil.Companion.datePickerDialog2(
                        MemberManagerViewActivity.this,
                        new MaterialDailogUtil.OnDialogSelectListner() {
                            @Override
                            public void onSelect(String s) {
                                etBirth.setText(s);
                            }
                        }
                );
                break;


            case R.id.member_activity_view_btn_isexecutives:
            case R.id.member_activity_view_et_isexecutives:
                MaterialDailogUtil.Companion.simpleListDialog(
                        MemberManagerViewActivity.this,
                        R.array.new_excutive,
                        new MaterialDailogUtil.OnDialogSelectListner() {
                            @Override
                            public void onSelect(String s) {
                                etIsExecutives.setText(MaterialDailogUtil.Companion.getExcutive(Integer.valueOf(s)));
                            }
                        });
                break;

            case R.id.member_activity_view_btn_new:
            case R.id.member_activity_view_et_new:
                MaterialDailogUtil.Companion.simpleListDialog(
                        MemberManagerViewActivity.this,
                        R.array.new_option,
                        new MaterialDailogUtil.OnDialogSelectListner() {
                            @Override
                            public void onSelect(String s) {
                                if (s.equals("0")) isFstMember = true;
                                else isFstMember = false;
                                if (s.equals("0"))
                                    isFstMember = false;
                                else
                                    isFstMember = true;
                                etNew.setText(MaterialDailogUtil.Companion.getNew(Integer.valueOf(s)));
                            }
                        });
                break;

            case R.id.member_activity_view_btn_gender:
            case R.id.member_activity_view_et_gender:
                MaterialDailogUtil.Companion.simpleListDialog(
                        MemberManagerViewActivity.this,
                        R.array.gender_option,
                        new MaterialDailogUtil.OnDialogSelectListner() {
                            @Override
                            public void onSelect(String s) {
                                etGender.setText(MaterialDailogUtil.Companion.getGender(Integer.valueOf(s)));
                            }
                        });
                break;

            case R.id.member_activity_view_btn_group:
            case R.id.member_activity_view_et_group:

                Common.hideKeyboard((Activity) v.getContext());
                Boolean aBoolean = true;
                if (aBoolean) break;

                //@Deprecated : 그룹이동 불가능 하도록 수정
                MaterialDailogUtil.Companion.getGroupDialog(v.getContext(),
                        new SimpleListener.OnCompleteListener() {
                            @Override
                            public void onComplete() {
                                etGroup.setText(CommonData.getSelectedGroup().name);
                                CommonData.setSelectedTeam(null);
                                etTeam.setText("");
                            }
                        });
                break;

            case R.id.member_activity_view_et_team:
            case R.id.member_activity_view_btn_team:

                Common.hideKeyboard((Activity) v.getContext());
                MaterialDailogUtil.Companion.getTeamDialog(v.getContext(),
                        new SimpleListener.OnCompleteListener() {
                            @Override
                            public void onComplete() {
                                LoggerHelper.d("CommonData.getSelectedTeam().name : " + CommonData.getSelectedTeam().name);
                                etTeam.setText(CommonData.getSelectedTeam().name + "  " + CommonData.getSelectedTeam().etc + "  ");
                            }
                        });
                break;
            case R.id.top_bar_btn_ok:
                Common.hideKeyboard(MemberManagerViewActivity.this);

                if (etTeam.getText().toString() == null) {
                    SuperToastUtil.toastE(MemberManagerViewActivity.this,
                            CommonString.INFO_TITLE_CONTROL_TEAM_OR_GROUP);
                    isSaving = false;
                    return;
                }
                if (!isSaving) {
                    isSaving = true;
                    sendMemberData();
                } else {
                    SuperToastUtil.toastE(this, "저장중입니다. 잠시만 기다리세요.");
                }
                break;

            case R.id.member_activity_view_btn_call:
                mTell = etPhone.getText().toString().replace("-", "");
                //AppUtil.sendCall(MemberManagerViewActivity.this, strTell, true);
                Common.sendDirectCall(mTell,this);

                break;
            case R.id.top_bar_btn_back:

                finish();
                //goBackHistory();
                break;


            case R.id.member_activity_view_imageivew_profile:

                onProfileClick();
                break;
            default:
                break;
        }
    }



    /**
     * 권한 요청에 대한 응답을 이곳에서 가져온다.
     *
     * @param requestCode 요청코드 * @param permissions 사용자가 요청한 권한들 * @param grantResults 권한에 대한 응답들(인덱스별로 매칭)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) { // 요청한 권한을 사용자가 "허용" 했다면...
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mTell));
                // Add Check Permission
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
            } else {
                Toast.makeText(MemberManagerViewActivity.this, "권한요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

package com.buel.holyhelper.view.recyclerView.memberShipRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.buel.holyhelper.R;
import com.buel.holyhelper.data.AdminMode;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.data.UserType;
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.model.UserModel;
import com.buel.holyhelper.view.SimpleListener;
import com.buel.holyhelper.view.activity.BaseActivity;
import com.commonLib.MaterialDailogUtil;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MemberShipRecyclerViewActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MemberRecyclerViewActivity";
    RecyclerView recyclerView;
    RecyclerView.Adapter<MemberShipRecyclerViewHolder> holderAdapter;
    ArrayList<UserModel> userModels = new ArrayList<>();
    String userType;
    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        super.setTopOkBtnVisibled(View.INVISIBLE);
        Intent intent = getIntent();
        userType = intent.getExtras().getString("type");

        FDDatabaseHelper.INSTANCE.getSubAdminList(UserType.SUB_ADMIN.toString(), userModelList -> {
            userModels = userModelList;
            setRecyclerVeiw();
        });
    }

    private void setRecyclerVeiw() {
        recyclerView = findViewById(R.id.recycler_view_main);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0)
                    MemberShipRecyclerViewActivity.super.setAllFabVisibled(true);
                else if (dy > 0)
                    MemberShipRecyclerViewActivity.super.setAllFabVisibled(false);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                /*if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }*/
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        findViewById(R.id.recycler_view_main_iv_close).setOnClickListener(this);
        super.setTopLayout(this);
        super.setBaseFloatingActionButton();
        super.setFabSnd(ViewMode.BRIEFING);
        super.setFabFst(ViewMode.ATTENDANCE);
        //super.setFabBackImg(super.fabFstBtn, R.drawable.ic_atten_check);
        super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_add_member);

        try {
            userModels.size();
        } catch (Exception e) {
            Toast.makeText(this, "USER 목록이 없습니다.", Toast.LENGTH_SHORT).show();
            goSettings();
            return;
        }

        holderAdapter = new MemberShipRecyclerViewAdapter(MemberShipRecyclerViewActivity.this , userModels, this);
        recyclerView.setAdapter(holderAdapter);
        setAdminMode();
    }

    @Override
    public void setHelperButton() {

        String strHelper = "";

        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            strHelper = "<strong>† 수정/관리</strong><br> " +
                    "버튼을 클릭하여 그룹 선택하세요." + "<br><br>" +
                    "상단 설정버튼을 클릭하면 <br>그룹 수정/삭제 관리가 가능합니다." + "<br><br>" +

                    "<strong>† 추가 기능</strong><br> " +
                    "하단의 그룹 추가 버튼을 클릭하면 <br>그룹추가가 가능합니다.<br><br>";
        }


        MaterialDailogUtil.noticeDialog(
                MemberShipRecyclerViewActivity.this,
                strHelper,
                CommonString.INFO_HELPER_TITLE,
                s -> {
                    CommonData.setIsFstEnter(false);
                    LoggerHelper.d("CommonData.getIsFstEnter() : " + CommonData.getIsFstEnter());
                });
    }

    @Override
    public void setActionButton() {
        CommonData.setViewMode(ViewMode.ADMIN);
        goSetGroup();
        CommonData.setHistoryClass((Class)MemberShipRecyclerViewActivity.class);
    }

    @Override
    public void setFstFabBtn() {
        CommonData.setViewMode(ViewMode.ATTENDANCE);
        goMemberRecycler();
    }

    @Override
    public void setSndFabBtn() {
        CommonData.setViewMode(ViewMode.BRIEFING);
        goMain();
    }

    private void setModifyMode() {
        //super.setTopOkBtnVisibled(View.VISIBLE);
        super.setTopOkBtnBackground(R.drawable.ic_clear_white_24dp);
        //setViewMode(ViewMode.MODIFY, "[ " + CommonData.getHolyModel().name + " ] 멤버쉽 리스트 수정");
        setTitle ( "[ " + CommonData.getHolyModel().name + " ] 멤버쉽 리스트 수정");
        CommonData.setAdminMode(AdminMode.MODIFY);
    }

    private void setAdminMode() {
        //super.setTopOkBtnVisibled(View.VISIBLE);
        super.setTopOkBtnBackground(R.drawable.ic_m_settings_24dp);
        setViewMode(ViewMode.ADMIN, "[ " +userType + " ] 계정수는 총 [ " + userModels.size() + " ] 개");
    }

    private void setViewMode(ViewMode mode, String title) {
        CommonData.setViewMode(mode);
        setTitle(title);

        if (holderAdapter != null)
            holderAdapter.notifyDataSetChanged();
    }

    private void setTitle(String str) {
        super.setTopTitleDesc(str);
    }

    /**
     * floating
     *
     * @param context
     */
    private void setFloatingActionButton(Context context) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.recycler_view_item_rl_main) {
            LoggerHelper.d("MemberShipRecyclerViewActivity", "v.getId()  : 2 " + v.getId());
            FDDatabaseHelper.INSTANCE.getMyCorps(new SimpleListener.OnCompleteListener() {
                @Override
                public void onComplete() {

                }
            });
        } else if (v.getId() == R.id.recycler_view_item_btn_delete) {

            Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
            /*LoggerHelper.d("MemberShipRecyclerViewActivity", "v.getId()  : 1 " + v.getId());
            FDDatabaseHelper.getMyCorps(new FDDatabaseHelper.onFDDCallbackListener() {
                @Override
                public void onFromDataComplete(int DataCode, DataSnapshot dataSnapshot) {
                    holderAdapter.notifyDataSetChanged();
                    try {
                        ((MemberShipRecyclerViewAdapter) holderAdapter).setItemArrayList(userModels);
                        recyclerView.refreshDrawableState();
                    } catch (Exception e) {
                        Toast.makeText(MemberShipRecyclerViewActivity.this, "그룹리스트가 없습니다.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MemberShipRecyclerViewActivity.this, SelectViewActivity.class));
                        finish();
                    }
                }
            });*/
        } else if (v.getId() == R.id.recycler_view_item_btn_select) {

            Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
            /*
            if (CommonData.getViewMode() == ViewMode.ADMIN) {

            } else if (CommonData.getViewMode() == ViewMode.MODIFY) {

            }*/
        } else if (v.getId() == R.id.top_bar_btn_back) {
            goSettings();
        } else if (v.getId() == R.id.top_bar_btn_ok) {

            Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
            return;
            /*
            if (CommonData.getViewMode() == ViewMode.ADMIN)
                setModifyMode();
            else if (CommonData.getViewMode() == ViewMode.MODIFY)
                setAdminMode();*/
        }
    }
}

package com.buel.holyhelper.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.buel.holyhelper.MainActivity;
import com.buel.holyhelper.R;
import com.buel.holyhelper.data.AdminMode;
import com.buel.holyhelper.data.AnalMode;
import com.buel.holyhelper.data.CommonData;
import com.buel.holyhelper.data.CommonString;
import com.buel.holyhelper.data.FDDatabaseHelper;
import com.buel.holyhelper.data.ViewMode;
import com.buel.holyhelper.utils.CorpsSettingManager;
import com.buel.holyhelper.view.MainViewPager;
import com.buel.holyhelper.view.SimpleListener;
import com.buel.holyhelper.view.recyclerView.briefingRecyclerView.BriefingRecyclerViewActivity;
import com.buel.holyhelper.view.recyclerView.foldcellRecyclerView.FoldCellRecyclerViewActivity;
import com.buel.holyhelper.view.recyclerView.groupRecyclerListView.RecyclerViewActivity;
import com.buel.holyhelper.view.recyclerView.memberRecyclerListView.MemberRecyclerViewActivity;
import com.buel.holyhelper.view.recyclerView.teamRecyclerListView.TeamRecyclerViewActivity;
import com.commonLib.Common;
import com.commonLib.MaterialDailogUtil;
import com.commonLib.SuperToastUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.logger.LoggerHelper;

import org.apache.poi.ss.formula.functions.T;

import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.listener.OnViewInflateListener;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //deleteProgressDialog();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //fullscreen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setProgressDialog(null);
    }


    /**
     * floating
     *
     * @param context
     */
    FloatingActionButton switchFab;
    public FloatingActionButton fab4thBtn;
    public FloatingActionButton fab3thBtn;
    public FloatingActionButton fab2ndBtn;
    public FloatingActionButton fabFstBtn;
    public FloatingActionButton fabFstActBtn;
    public FloatingActionButton fabHelper;
    public LinearLayout fab_back_ll;
    public Button fab3thTooltipBtn;
    public Button fab4thTooltipBtn;
    public Button fab2ndTooltipBtn;
    public Button fabFstTooltipBtn;
    public Boolean isPageAction = true;
    public Boolean isSaving = false;

    @SuppressLint("RestrictedApi")
    public void setBaseFloatingActionButton() {
        //LoggerHelper.d("setBaseFloatingActionButton!!!");
        View view = Common.getRootView(BaseActivity.this);

        switchFab = view.findViewById(R.id.switch_fab);
        fab4thBtn = view.findViewById(R.id.fab_4th_btn);
        fab3thBtn = view.findViewById(R.id.fab_3th_btn);
        fab2ndBtn = view.findViewById(R.id.fab_2nd_btn);
        fabFstBtn = view.findViewById(R.id.fab_fst_btn);
        fabFstActBtn = view.findViewById(R.id.fab_fst_act_btn);
        fabHelper = view.findViewById(R.id.fab_helper);
        fab4thTooltipBtn = view.findViewById(R.id.fab_4th_tooltip_btn);
        fab3thTooltipBtn = view.findViewById(R.id.fab_3th_tooltip_btn);
        fab2ndTooltipBtn = view.findViewById(R.id.fab_2nd_tooltip_btn);
        fabFstTooltipBtn = view.findViewById(R.id.fab_fst_tooltip_btn);
        fab_back_ll = view.findViewById(R.id.fab_back_ll);

        setFabBackColor(fabFstActBtn, R.color.blue400);
        setFabBackColor(fabHelper, R.color.blue400);
        fab_back_ll.setOnClickListener(this);
        switchFab.setOnClickListener(this);

        fab4thBtn.setOnClickListener(this);
        fab3thBtn.setOnClickListener(this);
        fab2ndBtn.setOnClickListener(this);
        fabFstBtn.setOnClickListener(this);

        fabFstActBtn.setOnClickListener(this);
        fabHelper.setOnClickListener(this);

        fab4thBtn.setVisibility(View.GONE);
        fab4thTooltipBtn.setVisibility(View.GONE);
    }

    public void setFabSnd(ViewMode viewMode) {
        int drawble = 0;
        String name = "";

        switch (viewMode) {
            case ATTENDANCE:
                drawble = R.drawable.ic_atten_check;
                name = CommonString.INFO_ATTEND_TITLE;
                break;
            case BRIEFING:
                drawble = R.drawable.ic_anal;
                name = CommonString.INFO_ANAL_TITLE;
                break;
            case ADMIN:
                drawble = R.drawable.ic_select_btn;
                name = CommonString.INFO_SELECT_TITLE;
                break;
            default:
                break;
        }

        setFabBackImg(fab2ndBtn, drawble);
        fab2ndTooltipBtn.setText(name);
    }

    public void setFabHelper() {
        int drawble = R.drawable.ic_share;
        setFabBackImg(fabHelper, drawble);
    }

    public void setFabFst(ViewMode viewMode) {

        int drawble = 0;
        String name = "";

        switch (viewMode) {
            case ATTENDANCE:
                drawble = R.drawable.ic_atten_check;
                name = CommonString.INFO_ATTEND_TITLE;
                break;
            case BRIEFING:
                drawble = R.drawable.ic_anal;
                name = CommonString.INFO_ANAL_TITLE;
                break;
            case ADMIN:
                drawble = R.drawable.ic_select_btn;
                name = CommonString.INFO_SELECT_TITLE;
                break;
            default:
                break;
        }

        setFabBackImg(fabFstBtn, drawble);
        fabFstTooltipBtn.setText(name);
    }

    public void setFabBackImg(FloatingActionButton fabtn, int drawble) {
        fabtn.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), drawble));
    }

    public void setFabBackColor(FloatingActionButton fabtn, int color) {
        fabtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(color)));
    }

    //FancyShowCaseView fancy 가이드를 보여줍니다.
    public void setGuideDailogAndOpenFabset(String ment, String title, FloatingActionButton targetfab) {
        MaterialDailogUtil.Companion.simpleDoneDialog(
                BaseActivity.this,
                title,
                ment,
                s -> {
                    setFabOpen(true);
                    setFabVisibled();

                    int targetX = (int) targetfab.getX() + targetfab.getWidth() / 2;
                    int targetY = (int) targetfab.getY() + targetfab.getHeight() / 2;
                    int rodius = (int) targetfab.getWidth() / 2 + 10;

                    new FancyShowCaseView.Builder(BaseActivity.this)
                            .focusOn(targetfab)
                            .focusCircleRadiusFactor(1)
                            .customView(R.layout.layout_my_custom_view, new OnViewInflateListener() {
                                @Override
                                public void onViewInflated(View view) {
                                    TextView textView = view.findViewById(R.id.tv_custom_view);
                                    textView.setText(ment);
                                }
                            })
                            .focusCircleAtPosition(targetX, targetY, rodius)
                            .build()
                            .show();
                });
    }

    public void setFocusEditText(EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //LoggerHelper.d("BASE ACTIVITY onFocusChange");
                if (!hasFocus) {
                    try {
                        Common.hideKeyboard((Activity) v.getContext());
                    } catch (Exception e) {
                        LoggerHelper.e(e.getMessage());
                    }
                }
            }
        });
    }

    public void setTopOkBtnBackground(int icon) {
        findViewById(R.id.top_bar_btn_ok).setBackgroundResource(icon);
    }

    public void setTopOkBtnVisibled(int visible) {
        findViewById(R.id.top_bar_btn_ok).setVisibility(visible);
    }

    public void setTopTitleDesc(String title) {

        LoggerHelper.d("setTopTitleDesc title : " + title);

        ((TextView) findViewById(R.id.top_bar_tv_desc)).setText(title);
    }

    public void setTopDetailDesc(int visible, String str) {
        LoggerHelper.d("setTopDetailDesc visible : " + visible);
        ((TextView) findViewById(R.id.top_bar_tv_detail_desc)).setVisibility(visible);
        if (str != null) ((TextView) findViewById(R.id.top_bar_tv_detail_desc)).setText(str);
        findViewById(R.id.top_bar_tv_detail_desc).setOnClickListener(this);
        findViewById(R.id.top_bar_tv_desc).setOnClickListener(this);
    }

    public void setTopLayout(View.OnClickListener onClickListener) {
        findViewById(R.id.top_bar_btn_ok).setOnClickListener(onClickListener);
        findViewById(R.id.top_bar_btn_back).setOnClickListener(onClickListener);
    }

    //뒤로가기 2번 클릭 시 종료
    private long lastTimeBackPressed; //뒤로가기 버튼이 클릭된 시간

    @Override
    public void onBackPressed() {
        //2초 이내에 뒤로가기 버튼을 재 클릭 시 앱 종료
        if (System.currentTimeMillis() - lastTimeBackPressed < 2000) {
            finish();
            return;
        }
        //'뒤로' 버튼 한번 클릭 시 메시지
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();

        //lastTimeBackPressed에 '뒤로'버튼이 눌린 시간을 기록
        lastTimeBackPressed = System.currentTimeMillis();
    }


    public void showProgressDialog(Boolean value) {

        /*if(MaterialDailogUtil.isProgressSupport()){
            setProgressDialog(null);
        }*/

        if (value)
            MaterialDailogUtil.Companion.showProgressDialog(true);
        else
            MaterialDailogUtil.Companion.showProgressDialog(false);
    }

    public void setProgressDialog(String str) {
        if (str == null)
            str = "잠시만 기다려 주세요.";
        MaterialDailogUtil.Companion.progressDialog(
                BaseActivity.this,
                "",
                str,
                false, false);
    }

    public void deleteProgressDialog() {
        MaterialDailogUtil.Companion.deleteProgressDialog();
    }

    public void setAllFabVisibled(Boolean visibled) {
        isFabOpen = false;
        setFabVisibled();
        if (visibled) {
            switchFab.show();
        } else {
            switchFab.hide();
        }
    }

    public void setFabVisibled() {
        Boolean visibled = isFabOpen;
        //LoggerHelper.d("setFabVisibled !!!visibled : " + visibled);
        if (visibled) {
            //setFabBackImg(switchFab, R.drawable.ic_clear);
            if (isPageAction) fabFstActBtn.show();
            fabHelper.show();
            fab2ndBtn.show();
            fabFstBtn.show();
            fab3thBtn.show();
            //fab4thBtn.show();
            fabFstTooltipBtn.setVisibility(View.VISIBLE);
            fab2ndTooltipBtn.setVisibility(View.VISIBLE);
            fab3thTooltipBtn.setVisibility(View.VISIBLE);
            //fab4thTooltipBtn.setVisibility(View.VISIBLE);
            fab_back_ll.setVisibility(View.VISIBLE);
        } else {
            switchFab.show();
           //setFabBackImg(switchFab, R.drawable.ic_apps);
            fab2ndBtn.hide();
            fab3thBtn.hide();
            //fab4thBtn.hide();
            fabFstActBtn.hide();
            fabHelper.hide();
            fabFstBtn.hide();
            fabFstTooltipBtn.setVisibility(View.INVISIBLE);
            fab2ndTooltipBtn.setVisibility(View.INVISIBLE);
            fab3thTooltipBtn.setVisibility(View.INVISIBLE);
            //fab4thTooltipBtn.setVisibility(View.INVISIBLE);
            fab_back_ll.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 선택 / 세팅 화면
     */
    public void setSelectView() {
        goSelect();
    }

    public void setQuickSelectView() {
        CorpsSettingManager.setGroup(this, new SimpleListener.OnCompleteListener() {
            @Override
            public void onComplete() {
                goMain();
            }
        });
    }

    public void setAlnalyticeMode() {
        MaterialDailogUtil.Companion.simpleListDialog(
                BaseActivity.this,
                R.array.anal_mode,
                s -> {
                    if (s.equals("0")) CommonData.setAnalMode(AnalMode.TEAM_MODE);
                    else CommonData.setAnalMode(AnalMode.GROP_MODE);
                    popToast("통계모드가 " + CommonData.getAnalMode() + " 로 변경되었습니다.");
                    goMain();
                });
    }

    public void setAddMemberView() {
        //CommonData.setViewMode(ViewMode.ADMIN);
        CommonData.setAdminMode(AdminMode.NORMAL);
        goSetAddMember();
    }

    /**
     * 출석 화면
     */
    public void setAttendanceView() {
        if (CommonData.getGroupModel() == null || CommonData.getTeamModel() == null) {
            SuperToastUtil.toastE(BaseActivity.this, CommonString.INFO_TITLE_CONTROL_TEAM_OR_GROUP);
            goSelect();
        } else {
            goMemberRecycler();
        }
    }

    public Boolean getFabOpen() {
        return isFabOpen;
    }

    public void setFabOpen(Boolean fabOpen) {
        isFabOpen = fabOpen;
    }

    private Boolean isFabOpen = false;

    public void setToolbarText() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.switch_fab) {
            //LoggerHelper.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!switch_fab");
            isFabOpen = !isFabOpen;
        } else if (id == R.id.fab_4th_btn) {
            set4thFabBtn();
            isFabOpen = false;
        } else if (id == R.id.fab_3th_btn) {
            set3thFabBtn();
            isFabOpen = false;
        } else if (id == R.id.fab_2nd_btn) {
            setSndFabBtn();
            isFabOpen = false;
        } else if (id == R.id.fab_fst_btn) {
            setFstFabBtn();
            isFabOpen = false;
        } else if (id == R.id.fab_fst_act_btn) {
            isFabOpen = false;
            setActionButton();
        } else if (id == R.id.fab_back_ll) {
            isFabOpen = false;
        } else if (id == R.id.fab_helper) {
            isFabOpen = false;
            setHelperButton();
        }
        setFabVisibled();
    }

    public void setHelperButton() {
    }

    public void set4thFabBtn() {
        setAlnalyticeMode();
    }

    public void set3thFabBtn() {
        //CommonData.setViewMode(ViewMode.ADMIN);
        setAddMemberView();
    }

    public void setSndFabBtn() {
        CommonData.setViewMode(ViewMode.ADMIN);
        setQuickSelectView();
    }

    public void setFstFabBtn() {
        CommonData.setViewMode(ViewMode.ATTENDANCE);
        setAttendanceView();
    }

    public void setActionButton() {
        MainViewPager.setDataAndTime(
                s -> {
                    if (s.equals("onComplete")) {
                        FDDatabaseHelper.INSTANCE.getAttend(() -> setToolbarText());
                    }
                });
    }

    public void popToast(String value) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
        LoggerHelper.d("popToast : " + value);
    }

    public void sToast(String value) {
        SuperToastUtil.toast(this, value);
    }

    public void sToast(String value, Boolean isError) {
        if (isError)
            SuperToastUtil.toastE(this, value);
        else
            SuperToastUtil.toast(this, value);
    }

    public void goBackHistory(Class<T> hitoryClass) {
        if (CommonData.isTutoMode()) {
            MaterialDailogUtil.Companion.simpleYesNoDialog(BaseActivity.this, "튜토리얼을 완료해주세요!", new MaterialDailogUtil.OnDialogSelectListner() {
                @Override
                public void onSelect(String s) {
                }
            });
        } else {
            //CommonData.setHistoryClass(hitoryClass);
            goBackHistoryIntent();
        }
    }

    public void goBackHistory() {
        if (CommonData.isTutoMode()) {
            MaterialDailogUtil.Companion.simpleDoneDialog(BaseActivity.this, "튜토리얼을 완료해주세요!.", new MaterialDailogUtil.OnDialogSelectListner() {
                @Override
                public void onSelect(String s) {

                }
            });
        } else {
            goBackHistoryIntent();
        }
    }


    /********************************************************************************************************************************************************
     * 화면 이동 관련
     *********************************************************************************************************************************************************/
    public void goBackHistoryIntent() {
        Intent intent;
        if (CommonData.getHistoryClass() == null) {
            onBackPressed();
        } else {
            intent = new Intent(getApplicationContext(), CommonData.getHistoryClass());
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    public void goLogout(String title) {
        MaterialDailogUtil.Companion.simpleDoneDialog(BaseActivity.this, title, new MaterialDailogUtil.OnDialogSelectListner() {
            @Override
            public void onSelect(String s) {

                CommonData.setInitCommonSettings();

                // Firebase sign out
                FirebaseAuth.getInstance().signOut();
                GoogleSignInClient googleSignInClient = CommonData.getGoogleSignInClient();
                // Google sign out
                googleSignInClient.signOut().addOnCompleteListener(BaseActivity.this,
                        task -> goLogin());
            }
        });
    }

    public void goLogout() {
        MaterialDailogUtil.Companion.simpleDoneDialog(BaseActivity.this, "진입경로가 잘못되었습니다.앱을 재실행 해야합니다.", new MaterialDailogUtil.OnDialogSelectListner() {
            @Override
            public void onSelect(String s) {

                CommonData.setInitCommonSettings();

                // Firebase sign out
                FirebaseAuth.getInstance().signOut();
                GoogleSignInClient googleSignInClient = CommonData.getGoogleSignInClient();
                // Google sign out
                googleSignInClient.signOut().addOnCompleteListener(BaseActivity.this,
                        task -> finish());
            }
        });
    }

    public void goJoin() {
        CommonData.setHistoryClass((Class) getApplicationContext().getClass());
        Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
        goStartAcitivity(intent);
    }


    public void goMain() {
        CommonData.setHistoryClass(null);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        goStartAcitivity(intent);
    }

    public void goBriefing() {
        Intent intent = new Intent(getApplicationContext(), BriefingRecyclerViewActivity.class);
        goStartAcitivity(intent);
    }

    public void goSettings() {
        CommonData.setHistoryClass((Class) MainActivity.class);
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        goStartAcitivity(intent);
    }

    public void goLogin() {
        CommonData.setHistoryClass((Class) getApplicationContext().getClass());
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        //intent.putExtra("login", "out");
        goStartAcitivity(intent);
    }

    public void goSelect() {
        CommonData.setHistoryClass((Class) MainActivity.class);
        Intent intent = new Intent(getApplicationContext(), SelectViewActivity.class);
        goStartAcitivity(intent);
    }

    public void goSetTeam() {
        CommonData.setHistoryClass((Class) SelectViewActivity.class);
        Intent intent = new Intent(getApplicationContext(), TeamManagerViewActivity.class);
        goStartAcitivity(intent);
    }

    public void goSetGroup() {
        CommonData.setHistoryClass((Class) SelectViewActivity.class);
        Intent intent = new Intent(getApplicationContext(), GroupManagerViewActivity.class);
        goStartAcitivity(intent);
    }

    public void goGroupRecycler() {
        CommonData.setHistoryClass((Class) getApplicationContext().getClass());
        Intent intent = new Intent(getApplicationContext(), RecyclerViewActivity.class);

        goStartAcitivity(intent);
    }

    public void goTeamRecycler() {
        CommonData.setHistoryClass((Class) getApplicationContext().getClass());
        Intent intent = new Intent(getApplicationContext(), TeamRecyclerViewActivity.class);
        goStartAcitivity(intent);
    }

    public void goMemberRecycler() {
        CommonData.setHistoryClass((Class) getApplicationContext().getClass());
        Intent intent = new Intent(getApplicationContext(), MemberRecyclerViewActivity.class);
        goStartAcitivity(intent);
    }

    public void goSetCorps() {
        CommonData.setHistoryClass((Class) SelectViewActivity.class);
        Intent intent = new Intent(getApplicationContext(), CorpsManagerViewActivity.class);
        goStartAcitivity(intent);
    }

    public void goSearchAddress() {
        Intent intent = new Intent(getApplicationContext(), DaumWebViewActivity.class);
        LoggerHelper.d("goStartAcitivity", intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }


    public void goManageMentPage() {
        CommonData.setHistoryClass((Class) SelectViewActivity.class);
        Intent intent = new Intent(getApplicationContext(), FoldCellRecyclerViewActivity.class);
        goStartAcitivity(intent);
    }

    public void goEmpty() {
        CommonData.setHistoryClass((Class) MainActivity.class);
        Intent intent = new Intent(getApplicationContext(), EmptyActivity.class);
        goStartAcitivity(intent);
    }

    public void goTutorial() {
        Intent intent = new Intent(getApplicationContext(), TutorialViewActivity.class);
        startActivity(intent);
    }

    public void goAgreement() {
        CommonData.setHistoryClass((Class) LoginActivity.class);
        Intent intent = new Intent(getApplicationContext(), AgreementActivity.class);
        goStartAcitivity(intent);
    }

    public void goSetAddMember() {
        //CommonData.setHistoryClass((Class) getApplicationContext().getClass());
        Intent intent = new Intent(getApplicationContext(), MemberManagerViewActivity.class);
        //goStartAcitivity(intent);
        LoggerHelper.d("goStartAcitivity", intent);

        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        //finish();
        //overridePendingTransition(0, 0);
    }

    private void goStartAcitivity(Intent intent, boolean isAni) {
        if (!isAni) intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    private void goStartAcitivity(Intent intent) {
        LoggerHelper.d("goStartAcitivity", intent);

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }
}

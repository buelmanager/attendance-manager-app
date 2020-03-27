package com.buel.holyhelpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.buel.holyhelpers.data.AdminMode;
import com.buel.holyhelpers.data.CommonData;
import com.buel.holyhelpers.data.CommonString;
import com.buel.holyhelpers.data.FDDatabaseHelper;
import com.buel.holyhelpers.data.PremiupType;
import com.buel.holyhelpers.data.TutorialViewerUtil;
import com.buel.holyhelpers.data.UserType;
import com.buel.holyhelpers.data.ViewMode;
import com.buel.holyhelpers.model.DateModel;
import com.buel.holyhelpers.model.UserModel;
import com.buel.holyhelpers.utils.AdmobUtils;
import com.buel.holyhelpers.utils.AppUtil;
import com.buel.holyhelpers.utils.CorpsSettingManager;
import com.buel.holyhelpers.view.CreateNavigationView;
import com.buel.holyhelpers.view.CreateToolbarView;
import com.buel.holyhelpers.view.DataTypeListener;
import com.buel.holyhelpers.view.MainViewPager;
import com.buel.holyhelpers.view.SimpleListener;
import com.buel.holyhelpers.view.activity.BaseActivity;
import com.commonLib.MaterialDailogUtil;
import com.commonLib.SuperToastUtil;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.orhanobut.logger.LoggerHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        BillingProcessor.IBillingHandler {

    private BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoggerHelper.i("MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CommonData.setViewMode(ViewMode.ADMIN);
        CommonData.setAdminMode(AdminMode.NORMAL);

        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //CommonData.setIsAdsOpen("true");
        //CommonData.setCurrentPremiumType(PremiupType.NORAML);

        setTitle("");

        if (CommonData.getIsFstEnter() && CommonData.getAppNotice() != null) {
            MaterialDailogUtil.Companion.noticeDialog(
                    MainActivity.this,
                    CommonData.getAppNotice(),
                    s -> {
                        CommonData.setIsFstEnter(false);
                        startMainPage();
                    });
        } else {
            startMainPage();
        }

        bp = new BillingProcessor(MainActivity.this, CommonString.GOOGLE_LISENCE_KEY, this);
        bp.initialize();
    }

    private boolean checkSetTutoMode() {
        if (CommonData.getHolyModel() == null) {
            MaterialDailogUtil.Companion.simpleDoneDialog(MainActivity.this,
                    "#1 단계, 교회 설정을 진행합니다.", s -> {
                        popToast(CommonString.CORP_NICK + " 설정 튜토리얼을 진행합니다.");
                        CommonData.setIsTutoMode(true);
                        goSetCorps();
                    });
            TutorialViewerUtil.getNewUserTutorialModels(MainActivity.this);
            return true;
        }
        return false;
    }

    private void startMainPage() {
        FDDatabaseHelper.INSTANCE.getMyCorps(new SimpleListener.OnCompleteListener() {
            @Override
            public void onComplete() {
                if (CommonData.getMemberShipType() == UserType.SUPER_ADMIN) {
                    if (checkSetTutoMode()) return;
                    checkSetTutoMode();
                }

                LoggerHelper.d("startMainPage");

                //Intent intent = new Intent(getApplicationContext(), FoldCellRecyclerViewActivity.class);
                //startActivity(intent);

                setLayout();
                FDDatabaseHelper.INSTANCE.getAttend(() -> setBriefingView());
            }
        });
    }

    @Override
    public void setHelperButton() {

        /**
         * <item>통계화면 도움말</item>
         *         <item>교적관리 도움말</item>
         *         <item>설정관련 도움말</item>
         *         <item>정보입력 도움말</item>
         */
        MaterialDailogUtil.Companion.simpleListDialog(
                MainActivity.this,
                R.array.main_hepler_option,
                R.string.select_helpoer,
                new MaterialDailogUtil.OnDialogSelectListner() {
                    @Override
                    public void onSelect(String s) {
                        if (s.equals("0"))
                            TutorialViewerUtil.getTeamAnalHelperTutorial(MainActivity.this);
                        else if (s.equals("1"))
                            TutorialViewerUtil.getMemberAccountAdminTutorialModels(MainActivity.this);
                        else if (s.equals("2"))
                            TutorialViewerUtil.getSelectionTutorialModels(MainActivity.this);
                        else if (s.equals("3"))
                            TutorialViewerUtil.getCreateAccountTutorialModels(MainActivity.this);
                    }
                });
    }


    /**
     * Main에 필요한 Layout을 그린다.
     */
    private void setLayout() {
        LoggerHelper.i("setLayout");
        CommonData.setViewMode(ViewMode.BRIEFING);

        setNavigationView(MainActivity.this);


        super.setBaseFloatingActionButton();
        super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_calendar);
        super.setFabSnd(ViewMode.ADMIN);
        super.setFabFst(ViewMode.ATTENDANCE);

        if (CommonData.getUserModel().permission.equals("no") && CommonData.getMemberShipType() == UserType.SUB_ADMIN) {
            try {
                LoggerHelper.d(CommonData.getHolyModel());

                if (CommonData.getHolyModel() == null) {
                    MaterialDailogUtil.Companion.simpleDoneDialog(
                            MainActivity.this,
                            CommonString.CORP_NICK + "정보 오류",
                            CommonString.CORP_NICK + " 정보가 정확하지 않습니다.",
                            new MaterialDailogUtil.OnDialogSelectListner() {
                                @Override
                                public void onSelect(String s) {
                                    goLogin();
                                }
                            });
                    return;
                }

                if (CommonData.getHolyModel().adminName == null) {
                    CommonData.getHolyModel().adminName = "";
                    CommonData.getHolyModel().adminEmail = "";
                }

                String strAdmin =
                        "\n최고 관리자 : " + CommonData.getHolyModel().adminName +
                                "\n[ 이메일 : " + CommonData.getHolyModel().adminEmail + " ] " +
                                "\n[ 연락처 : " + CommonData.getHolyModel().adminPhone + " ] ";

                MaterialDailogUtil.Companion.simpleDoneDialog(
                        MainActivity.this,
                        "권한이 없습니다.",
                        "관리자에게 권한요청을 하세요. \n" + strAdmin,
                        new MaterialDailogUtil.OnDialogSelectListner() {
                            @Override
                            public void onSelect(String s) {
                                CommonData.setInitCommonSettings();

                                // Firebase sign out
                                FirebaseAuth.getInstance().signOut();
                                GoogleSignInClient googleSignInClient = CommonData.getGoogleSignInClient();
                                // Google sign out
                                googleSignInClient.signOut().addOnCompleteListener(MainActivity.this,
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                goLogin();
                                            }
                                        });

                            }
                        });
            } catch (Exception e) {
                MaterialDailogUtil.Companion.simpleDoneDialog(
                        MainActivity.this,
                        "단체정보 오류",
                        CommonString.CORP_NICK + " 정보가 정확하지 않습니다.",
                        new MaterialDailogUtil.OnDialogSelectListner() {
                            @Override
                            public void onSelect(String s) {
                                goLogin();
                            }
                        });
            }
            return;
        }

        if (CommonData.getGroupModel() == null || CommonData.getTeamModel() == null) {
            String ment = CommonString.GUIDE_FLOATING_BUTTON_SELECT;
            String title = CommonString.INFO_TITLE_CONTROL_TEAM_OR_GROUP;
            super.setGuideDailogAndOpenFabset(ment, title, super.fab2ndBtn);
        }
    }

    /**
     * toolbar
     */
    CreateToolbarView toolbarView;

    public void setToolbar() {
        if (toolbarView == null) {
            toolbarView = new CreateToolbarView();
            toolbarView.setToolbar(MainActivity.this);

            Toolbar toolbar = toolbarView.getToolbar();
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CorpsSettingManager.setGroup(MainActivity.this, new SimpleListener.OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            CommonData.setHistoryClass(null);
                            goMain();
                        }
                    });
                }
            });
        }
    }

    public void setToolbarView() {
        toolbarView.setToolbarText();
    }

    /**
     * Navigation
     *
     * @param context
     */
    private void setNavigationView(Context context) {
        setToolbar();
        CreateNavigationView createNavigationView = new CreateNavigationView();
        createNavigationView.setView(MainActivity.this, new SimpleListener.OnCompleteListener() {
            @Override
            public void onComplete() {
                toolbarView.setToolbarText();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 메인화면 오른쪽 상단의 옵션버튼
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            goSettings();
            return true;
        } else if (id == R.id.action_send_to_sub_admin) {

            if (CommonData.getMemberShipType() != UserType.SUPER_ADMIN) {

                popToast("현재는 최고 관리자만 가능한 기능입니다.");
                return true;
            }

            MaterialDailogUtil.Companion.simpleInputDoneDialog(
                    MainActivity.this,
                    getString(R.string.action_send_sub_admin),
                    "운영 관리자에게 메세지를 보냅니다.",
                    new MaterialDailogUtil.OnDialogSelectListner() {
                        @Override
                        public void onSelect(String s) {

                            if (s == null || s.equals("")) return;

                            FDDatabaseHelper.INSTANCE.getSubAdminList(UserType.SUB_ADMIN.toString(), new DataTypeListener.OnCompleteListener<ArrayList<UserModel>>() {
                                @Override
                                public void onComplete(ArrayList<UserModel> userModels) {

                                    String userName = CommonData.getUserModel().userName + "님께서 메세지를 보냈습니다.";
                                    AppUtil.sendSubAdminPushMessage(MainActivity.this, userModels, userName, s);

                                }
                            });
                        }
                    });
        } else if (id == R.id.action_no_price) {
            MaterialDailogUtil.Companion.simpleInputDoneDialog(
                    MainActivity.this,
                    getString(R.string.action_no_price_notice),
                    "무료사용에 대한 사유를 간단히 적어주세요.",
                    strSendMsg -> {
                        if (strSendMsg == null || strSendMsg.equals("")) return;
                        UserModel userModel = CommonData.getUserModel();
                        userModel.message = strSendMsg;
                        FDDatabaseHelper.INSTANCE.sendUpdateSimpleDoc(userModel, "noprice",
                                aBoolean -> popToast("전달되었습니다. 연락드리겠습니다."));
                    });
        } else if (id == R.id.action_todeveloper) {
            MaterialDailogUtil.Companion.simpleInputDoneDialog(
                    MainActivity.this,
                    getString(R.string.action_todeveloper),
                    "간단한 내용을 여기에 적어주세요",
                    strSendMsg -> {
                        if (strSendMsg == null || strSendMsg.equals("")) return;
                        UserModel userModel = CommonData.getUserModel();
                        userModel.message = strSendMsg;
                        FDDatabaseHelper.INSTANCE.sendUpdateSimpleDoc(userModel, "todeveloper",
                                aBoolean -> popToast("개발자에게 전달되었습니다. 소중한 의견주셔서 감사합니다."));
                    });
        } else if (id == R.id.action_helper) {
            Uri uri = Uri.parse("https://cafe.naver.com/holyhelper");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 메인 화면
     */
    private void setBriefingView() {
        HashMap<String, DateModel> dateModelHashMap = CommonData.getAttendDateMaps();
        if (CommonData.getAttendDateMaps() == null) {
            String ment = CommonString.GUIDE_FLOATING_BUTTON_ATTEND;
            String title = CommonString.INFO_TITLE_DONT_ATTEND_DATA;
            super.setGuideDailogAndOpenFabset(ment, title, super.fabFstBtn);
        } else {
            //CommonData.getCurrentSelectViewPageNum()
            MainViewPager.setMainViewPage(MainActivity.this, 1);
        }
    }

    /**
     * onNavigationItemSelected
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_admin) {
            if (CommonData.getMemberShipType() == UserType.PERSONAL) {
                MaterialDailogUtil.Companion.simpleInputDoneDialog(MainActivity.this, "비번을 입력하세요.", "이름", new MaterialDailogUtil.OnDialogSelectListner() {
                    @Override
                    public void onSelect(String s) {

                        String strName = s;

                        if (CommonData.getHolyModel().password == null || CommonData.getHolyModel().password.equals("")) {
                            SuperToastUtil.toastE(MainActivity.this, "비밀번호가 설정되어있지 않습니다. 관리자에게 문의하세요.");
                            return;
                        }

                        if (CommonData.getHolyModel().password.equals(s)) {
                            CommonData.setViewMode(ViewMode.ADMIN);
                            setSelectView();
                        } else {
                            SuperToastUtil.toastE(MainActivity.this, "비밀번호가 맞지않습니다. 관리자에게 문의하세요.");
                            return;
                        }
                    }
                });
                return false;
            } else {
                CommonData.setViewMode(ViewMode.ADMIN);
                setSelectView();
            }
        } else if (id == R.id.nav_attendance) {
            if (CommonData.getMemberShipType() == UserType.PERSONAL) {
                MaterialDailogUtil.Companion.simpleInputDoneDialog(MainActivity.this, "비번을 입력하세요.", "이름", new MaterialDailogUtil.OnDialogSelectListner() {
                    @Override
                    public void onSelect(String s) {

                        String strName = s;

                        if (CommonData.getHolyModel().password == null || CommonData.getHolyModel().password.equals("")) {
                            SuperToastUtil.toastE(MainActivity.this, "비밀번호가 설정되어있지 않습니다. 관리자에게 문의하세요.");
                            return;
                        }


                        if (CommonData.getHolyModel().password.equals(s)) {
                            CommonData.setViewMode(ViewMode.ATTENDANCE);
                            setAttendanceView();
                        } else {
                            SuperToastUtil.toastE(MainActivity.this, "비밀번호가 맞지않습니다. 관리자에게 문의하세요.");
                            return;
                        }
                    }
                });
                return false;
            } else {
                CommonData.setViewMode(ViewMode.ATTENDANCE);
                //setAttendanceView();
                goManageMentPage();
            }
        } else if (id == R.id.nav_addmember) {

            if (CommonData.getMemberShipType() == UserType.PERSONAL) {
                MaterialDailogUtil.Companion.simpleInputDoneDialog(MainActivity.this, "비번을 입력하세요.", "이름", new MaterialDailogUtil.OnDialogSelectListner() {
                    @Override
                    public void onSelect(String s) {

                        if (CommonData.getHolyModel().password == null || CommonData.getHolyModel().password.equals("")) {
                            SuperToastUtil.toastE(MainActivity.this, "비밀번호가 설정되어있지 않습니다. 관리자에게 문의하세요.");
                            return;
                        }

                        if (CommonData.getHolyModel().password.equals(s)) {
                            CommonData.setViewMode(ViewMode.SEARCH_MEMBER);
                            String strName = "";
                            LoggerHelper.d("btnSelectSearchMember", "strName : " + strName);
                            CommonData.setStrSearch(strName);
                            goManageMentPage();
                        } else {
                            SuperToastUtil.toastE(MainActivity.this, "비밀번호가 맞지않습니다. 관리자에게 문의하세요.");
                            return;
                        }
                    }
                });
                return false;
            } else {
                CommonData.setViewMode(ViewMode.SEARCH_MEMBER);
                String strName = "";
                LoggerHelper.d("btnSelectSearchMember", "strName : " + strName);
                CommonData.setStrSearch(strName);
                goManageMentPage();
                CommonData.setHistoryClass((Class) MainActivity.this.getClass());
            }
        } else if (id == R.id.exit) {
            MaterialDailogUtil.Companion.simpleYesNoDialog(
                    MainActivity.this,
                    new MaterialDailogUtil.OnDialogSelectListner() {
                        @Override
                        public void onSelect(String s) {
                            finish();
                        }
                    });
            return true;
        } else if (id == R.id.nav_prayer_for) {
            CommonData.setCurrentSelectViewPageNum(0);
            goMain();
            return true;
        } else if (id == R.id.nav_prayer_plan) {
            CommonData.setCurrentSelectViewPageNum(1);
            goMain();
            return true;
        } else {
            if (CommonData.getHolyModel().cumulativePoint <= CommonData.getLevel2()) {
                SuperToastUtil.toastE(MainActivity.this, " 현재 ' App Level ' 에서는 이용하실수 없습니다.");
                return false;
            } else {
                goEmpty();
            }
        }

        finish();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {
        bp.loadOwnedPurchasesFromGoogle();
        if (bp.isSubscribed(CommonString.SUB_ADMIN_SUBSCRIBE_01)) {
            CommonData.setCurrentPremiumType(PremiupType.ADS_PREMIUM);
        } else {
            CommonData.setCurrentPremiumType(PremiupType.NORAML);
        }
        AdmobUtils.setBottomBannerAds(MainActivity.this);
    }
}




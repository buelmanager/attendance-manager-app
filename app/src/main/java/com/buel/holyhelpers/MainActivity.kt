package com.buel.holyhelpers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
import com.anjlab.android.iab.v3.TransactionDetails
import com.buel.holyhelpers.data.*
import com.buel.holyhelpers.data.CommonData.attendDateMaps
import com.buel.holyhelpers.data.CommonData.currentPremiumType
import com.buel.holyhelpers.data.CommonData.googleSignInClient
import com.buel.holyhelpers.data.CommonData.groupModel
import com.buel.holyhelpers.data.CommonData.holyModel
import com.buel.holyhelpers.data.CommonData.isTutoMode
import com.buel.holyhelpers.data.CommonData.memberShipType
import com.buel.holyhelpers.data.CommonData.setAdminMode
import com.buel.holyhelpers.data.CommonData.setInitCommonSettings
import com.buel.holyhelpers.data.CommonData.setViewMode
import com.buel.holyhelpers.data.CommonData.strSearch
import com.buel.holyhelpers.data.CommonData.teamModel
import com.buel.holyhelpers.data.FDDatabaseHelper.getAttend
import com.buel.holyhelpers.data.FDDatabaseHelper.getMyCorps
import com.buel.holyhelpers.data.FDDatabaseHelper.getSubAdminList
import com.buel.holyhelpers.data.FDDatabaseHelper.sendUpdateSimpleDoc
import com.buel.holyhelpers.model.UserModel
import com.buel.holyhelpers.utils.AdmobUtils
import com.buel.holyhelpers.utils.AppUtil
import com.buel.holyhelpers.utils.CorpsSettingManager
import com.buel.holyhelpers.view.*
import com.buel.holyhelpers.view.activity.BaseActivity
import com.commonLib.MaterialDailogUtil.Companion.noticeDialog
import com.commonLib.MaterialDailogUtil.Companion.simpleDoneDialog
import com.commonLib.MaterialDailogUtil.Companion.simpleInputDoneDialog
import com.commonLib.MaterialDailogUtil.Companion.simpleListDialog
import com.commonLib.MaterialDailogUtil.Companion.simpleYesNoDialog
import com.commonLib.MaterialDailogUtil.OnDialogSelectListner
import com.commonLib.SuperToastUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.logger.LoggerHelper
import org.apache.poi.ss.formula.functions.T
import java.util.*

class MainActivity() : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, IBillingHandler {
    private var bp: BillingProcessor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        LoggerHelper.i("MainActivity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewMode(ViewMode.ADMIN)
        setAdminMode(AdminMode.NORMAL)

        title = ""
        if (CommonData.isFstEnter && CommonData.appNotice != null) {
            noticeDialog(
                    this@MainActivity,
                    CommonData.appNotice,
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            CommonData.isFstEnter = false
                            startMainPage()
                        }
                    })
        } else {
            startMainPage()
        }
        bp = BillingProcessor(this@MainActivity, CommonString.GOOGLE_LISENCE_KEY, this)
        bp!!.initialize()
    }

    private fun checkSetTutoMode(): Boolean {
        if (holyModel == null) {
            simpleDoneDialog(this@MainActivity,
                    "#1 단계, 교회 설정을 진행합니다.", object : OnDialogSelectListner {
                override fun onSelect(s: String) {
                    popToast(CommonString.CORP_NICK + " 설정 튜토리얼을 진행합니다.")
                    isTutoMode
                    goSetCorps()
                }
            })
            TutorialViewerUtil.getNewUserTutorialModels(this@MainActivity)
            return true
        }
        return false
    }

    private fun startMainPage() {
        getMyCorps(SimpleListener.OnCompleteListener {
            if (memberShipType == UserType.SUPER_ADMIN) {
                if (checkSetTutoMode()) return@OnCompleteListener
                checkSetTutoMode()
            }

            LoggerHelper.d("startMainPage")

            setLayout()
            //getAttend(SimpleListener.OnCompleteListener { setBriefingView() })
        })
    }

    override fun setHelperButton() {
        /**
         * <item>통계화면 도움말</item>
         * <item>교적관리 도움말</item>
         * <item>설정관련 도움말</item>
         * <item>정보입력 도움말</item>
         */
        simpleListDialog(
                this@MainActivity,
                R.array.main_hepler_option,
                R.string.select_helpoer,
                object : OnDialogSelectListner {
                    override fun onSelect(s: String) {
                        if ((s == "0")) TutorialViewerUtil.getTeamAnalHelperTutorial(this@MainActivity) else if ((s == "1")) TutorialViewerUtil.getMemberAccountAdminTutorialModels(this@MainActivity) else if ((s == "2")) TutorialViewerUtil.getSelectionTutorialModels(this@MainActivity) else if ((s == "3")) TutorialViewerUtil.getCreateAccountTutorialModels(this@MainActivity)
                    }
                })
    }

    /**
     * Main에 필요한 Layout을 그린다.
     */
    private fun setLayout() {
        LoggerHelper.i("setLayout")
        setViewMode(ViewMode.BRIEFING)
        setNavigationView(this@MainActivity)
        super.setBaseFloatingActionButton()
        super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_calendar)
        super.setFabSnd(ViewMode.ADMIN)
        super.setFabFst(ViewMode.ATTENDANCE)
        if ((CommonData.userModel.permission == "no") && memberShipType == UserType.SUB_ADMIN) {
            try {
                LoggerHelper.d(holyModel)
                if (holyModel == null) {
                    simpleDoneDialog(
                            this@MainActivity,
                            CommonString.CORP_NICK + "정보 오류",
                            CommonString.CORP_NICK + " 정보가 정확하지 않습니다.",
                            object : OnDialogSelectListner {
                                override fun onSelect(s: String) {
                                    goLogin()
                                }
                            })
                    return
                }
                if (holyModel.adminName == null) {
                    holyModel.adminName = ""
                    holyModel.adminEmail = ""
                }
                val strAdmin = ("\n최고 관리자 : " + holyModel.adminName +
                        "\n[ 이메일 : " + holyModel.adminEmail + " ] " +
                        "\n[ 연락처 : " + holyModel.adminPhone + " ] ")
                simpleDoneDialog(
                        this@MainActivity,
                        "권한이 없습니다.",
                        "관리자에게 권한요청을 하세요. \n$strAdmin",
                        object : OnDialogSelectListner {
                            override fun onSelect(s: String) {
                                setInitCommonSettings()
                                // Firebase sign out
                                FirebaseAuth.getInstance().signOut()
                                val googleSignInClient = googleSignInClient
                                // Google sign out
                                googleSignInClient.signOut().addOnCompleteListener(this@MainActivity,
                                        object : OnCompleteListener<Void?> {
                                            override fun onComplete(task: Task<Void?>) {
                                                goLogin()
                                            }
                                        })
                            }
                        })
            } catch (e: Exception) {
                simpleDoneDialog(
                        this@MainActivity,
                        "단체정보 오류",
                        CommonString.CORP_NICK + " 정보가 정확하지 않습니다.",
                        object : OnDialogSelectListner {
                            override fun onSelect(s: String) {
                                goLogin()
                            }
                        })
            }
            return
        }
        if (groupModel == null || teamModel == null) {
            val ment = CommonString.GUIDE_FLOATING_BUTTON_SELECT
            val title = CommonString.INFO_TITLE_CONTROL_TEAM_OR_GROUP
            super.setGuideDailogAndOpenFabset(ment, title, super.fab2ndBtn)
        }
    }

    /**
     * toolbar
     */
    var toolbarView: CreateToolbarView? = null

    fun setToolbar() {
        if (toolbarView == null) {
            toolbarView = CreateToolbarView()
            toolbarView!!.setToolbar(this@MainActivity)
            val toolbar = toolbarView!!.toolbar
            toolbar.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    CorpsSettingManager.setGroup(this@MainActivity, object : SimpleListener.OnCompleteListener {
                        override fun onComplete() {
                            //CommonData.historyClass =
                            goMain()
                        }
                    })
                }
            })
        }
    }

    fun setToolbarView() {
        toolbarView!!.setToolbarText()
    }

    /**
     * Navigation
     *
     * @param context
     */
    private fun setNavigationView(context: Context) {
        setToolbar()
        val createNavigationView = CreateNavigationView()
        createNavigationView.setView(this@MainActivity, object : SimpleListener.OnCompleteListener {
            override fun onComplete() {
                toolbarView!!.setToolbarText()
            }
        })
    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /**
     * 메인화면 오른쪽 상단의 옵션버튼
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            goSettings()
            return true
        } else if (id == R.id.action_send_to_sub_admin) {
            if (memberShipType != UserType.SUPER_ADMIN) {
                popToast("현재는 최고 관리자만 가능한 기능입니다.")
                return true
            }
            simpleInputDoneDialog(
                    this@MainActivity,
                    getString(R.string.action_send_sub_admin),
                    "운영 관리자에게 메세지를 보냅니다.",
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            if (s == null || (s == "")) return
                            getSubAdminList(UserType.SUB_ADMIN.toString(), object : DataTypeListener.OnCompleteListener<ArrayList<UserModel>> {
                                /*override fun onComplete(userModels: ArrayList<UserModel?>) {
                                    val userName = CommonData.userModel.userName + "님께서 메세지를 보냈습니다."
                                    AppUtil.sendSubAdminPushMessage(this@MainActivity, userModels, userName, s)
                                }*/

                                override fun onComplete(userModels: ArrayList<UserModel>?) {
                                val userName = CommonData.userModel.userName + "님께서 메세지를 보냈습니다."
                                AppUtil.sendSubAdminPushMessage(this@MainActivity, userModels!!, userName, s)
                                }
                            })
                        }
                    })
        } else if (id == R.id.action_no_price) {
            simpleInputDoneDialog(
                    this@MainActivity,
                    getString(R.string.action_no_price_notice),
                    "무료사용에 대한 사유를 간단히 적어주세요.",
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            if (s == null || (s == "")) return
                            val userModel: UserModel = CommonData.userModel
                            userModel.message = s
                            sendUpdateSimpleDoc(userModel, "noprice",
                                    DataTypeListener.OnCompleteListener { aBoolean: Boolean? -> popToast("전달되었습니다. 연락드리겠습니다.") })
                        }
                    })
        } else if (id == R.id.action_todeveloper) {
            simpleInputDoneDialog(
                    this@MainActivity,
                    getString(R.string.action_todeveloper),
                    "간단한 내용을 여기에 적어주세요",
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            if (s == null || (s == "")) return
                            val userModel: UserModel = CommonData.userModel
                            userModel.message = s
                            sendUpdateSimpleDoc(userModel, "todeveloper",
                                    DataTypeListener.OnCompleteListener { aBoolean: Boolean? -> popToast("개발자에게 전달되었습니다. 소중한 의견주셔서 감사합니다.") })
                        }
                    })
        } else if (id == R.id.action_helper) {
            val uri = Uri.parse("https://cafe.naver.com/holyhelper")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 메인 화면
     */
    private fun setBriefingView() {
        val dateModelHashMap = attendDateMaps
        if (attendDateMaps == null) {
            val ment = CommonString.GUIDE_FLOATING_BUTTON_ATTEND
            val title = CommonString.INFO_TITLE_DONT_ATTEND_DATA
            super.setGuideDailogAndOpenFabset(ment, title, super.fabFstBtn)
        } else { //CommonData.getCurrentSelectViewPageNum()
            MainViewPager.setMainViewPage(this@MainActivity, 1)
        }
    }

    /**
     * onNavigationItemSelected
     *
     * @param item
     * @return
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.nav_admin) {
            if (memberShipType == UserType.PERSONAL) {
                simpleInputDoneDialog(this@MainActivity, "비번을 입력하세요.", "이름", object : OnDialogSelectListner {
                    override fun onSelect(s: String) {
                        val strName = s
                        if (holyModel.password == null || (holyModel.password == "")) {
                            SuperToastUtil.toastE(this@MainActivity, "비밀번호가 설정되어있지 않습니다. 관리자에게 문의하세요.")
                            return
                        }
                        if ((holyModel.password == s)) {
                            setViewMode(ViewMode.ADMIN)
                            setSelectView()
                        } else {
                            SuperToastUtil.toastE(this@MainActivity, "비밀번호가 맞지않습니다. 관리자에게 문의하세요.")
                            return
                        }
                    }
                })
                return false
            } else {
                setViewMode(ViewMode.ADMIN)
                setSelectView()
            }
        } else if (id == R.id.nav_attendance) {
            if (memberShipType == UserType.PERSONAL) {
                simpleInputDoneDialog(this@MainActivity, "비번을 입력하세요.", "이름", object : OnDialogSelectListner {
                    override fun onSelect(s: String) {
                        val strName = s
                        if (holyModel.password == null || (holyModel.password == "")) {
                            SuperToastUtil.toastE(this@MainActivity, "비밀번호가 설정되어있지 않습니다. 관리자에게 문의하세요.")
                            return
                        }
                        if ((holyModel.password == s)) {
                            setViewMode(ViewMode.ATTENDANCE)
                            setAttendanceView()
                        } else {
                            SuperToastUtil.toastE(this@MainActivity, "비밀번호가 맞지않습니다. 관리자에게 문의하세요.")
                            return
                        }
                    }
                })
                return false
            } else {
                setViewMode(ViewMode.ATTENDANCE)
                //setAttendanceView();
                goManageMentPage()
            }
        } else if (id == R.id.nav_addmember) {
            if (memberShipType == UserType.PERSONAL) {
                simpleInputDoneDialog(this@MainActivity, "비번을 입력하세요.", "이름", object : OnDialogSelectListner {
                    override fun onSelect(s: String) {
                        if (holyModel.password == null || (holyModel.password == "")) {
                            SuperToastUtil.toastE(this@MainActivity, "비밀번호가 설정되어있지 않습니다. 관리자에게 문의하세요.")
                            return
                        }
                        if ((holyModel.password == s)) {
                            setViewMode(ViewMode.SEARCH_MEMBER)
                            val strName = ""
                            LoggerHelper.d("btnSelectSearchMember", "strName : $strName")
                            strSearch = strName
                            goManageMentPage()
                        } else {
                            SuperToastUtil.toastE(this@MainActivity, "비밀번호가 맞지않습니다. 관리자에게 문의하세요.")
                            return
                        }
                    }
                })
                return false
            } else {
                setViewMode(ViewMode.SEARCH_MEMBER)
                val strName = ""
                LoggerHelper.d("btnSelectSearchMember", "strName : $strName")
                strSearch = strName
                goManageMentPage()
                CommonData.historyClass = MainActivity::class.java as Class<T>
            }
        } else if (id == R.id.exit) {
            simpleYesNoDialog(
                    this@MainActivity,
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            finish()
                        }
                    })
            return true
        } else if (id == R.id.nav_prayer_for) {
            CommonData.currentSelectViewPageNum = 0
            goMain()
            return true
        } else if (id == R.id.nav_prayer_plan) {
            CommonData.currentSelectViewPageNum = 1
            goMain()
            return true
        } else {
            if (holyModel.cumulativePoint <= CommonData.level2) {
                SuperToastUtil.toastE(this@MainActivity, " 현재 ' App Level ' 에서는 이용하실수 없습니다.")
                return false
            } else {
                goEmpty()
            }
        }
        finish()
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {}
    override fun onPurchaseHistoryRestored() {}
    override fun onBillingError(errorCode: Int, error: Throwable?) {}
    override fun onBillingInitialized() {
        bp!!.loadOwnedPurchasesFromGoogle()
        if (bp!!.isSubscribed(CommonString.SUB_ADMIN_SUBSCRIBE_01)) {
            currentPremiumType = PremiupType.ADS_PREMIUM
        } else {
            currentPremiumType = PremiupType.NORAML
        }
        AdmobUtils.setBottomBannerAds(this@MainActivity)
    }
}
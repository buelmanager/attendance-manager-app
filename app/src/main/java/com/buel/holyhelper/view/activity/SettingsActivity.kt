package com.buel.holyhelper.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.Constants
import com.anjlab.android.iab.v3.TransactionDetails
import com.buel.holyhelper.R
import com.buel.holyhelper.data.*
import com.buel.holyhelper.management.MemberManager
import com.buel.holyhelper.management.firestore.FireStoreMemberManager
import com.buel.holyhelper.management.firestore.FireStoreWriteManager
import com.buel.holyhelper.model.HolyModel
import com.buel.holyhelper.utils.AppUtil
import com.buel.holyhelper.utils.PoiUtil
import com.buel.holyhelper.utils.SortMapUtil
import com.buel.holyhelper.utils.U.groupCovertList
import com.buel.holyhelper.view.DataTypeListener
import com.buel.holyhelper.view.SimpleListener
import com.buel.holyhelper.view.recyclerView.memberShipRecyclerView.MemberShipRecyclerViewActivity
import com.commonLib.Common
import com.commonLib.MaterialDailogUtil
import com.commonLib.MaterialDailogUtil.Companion.simpleYesNoDialog
import com.commonLib.SuperToastUtil
import com.orhanobut.logger.LoggerHelper
import kotlinx.coroutines.runBlocking
import org.apache.poi.ss.formula.functions.T

class SettingsActivity : BaseActivity(), View.OnClickListener, BillingProcessor.IBillingHandler {

    private var bp: BillingProcessor? = null
    private var readyToPurchase = false

    internal var ExcelData: Intent? = null

    var missList: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<View>(R.id.preference_activity_ll_1).setOnClickListener(this)
        findViewById<View>(R.id.preference_activity_ll_2).setOnClickListener(this)
        findViewById<View>(R.id.preference_activity_ll_3).setOnClickListener(this)
        findViewById<View>(R.id.preference_activity_ll_4).setOnClickListener(this)
        findViewById<View>(R.id.preference_activity_ll_5).setOnClickListener(this)
        findViewById<View>(R.id.preference_activity_ll_6).setOnClickListener(this)
        findViewById<View>(R.id.preference_activity_ll_7).setOnClickListener(this)
        findViewById<View>(R.id.preference_activity_ll_8).setOnClickListener(this)
        findViewById<View>(R.id.preference_activity_ll_9).setOnClickListener(this)
        setTopLayout(this)
        super.setTopTitleDesc("Settings")
        super.setTopOkBtnVisibled(View.INVISIBLE)
        bp = BillingProcessor(this@SettingsActivity, CommonString.GOOGLE_LISENCE_KEY, this)
        bp!!.initialize()

    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.top_bar_btn_back ->
                //goBackHistoryIntent();
                goMain()

            R.id.preference_activity_ll_1 -> {

                CommonData.setAdminMode(AdminMode.MODIFY)
                goJoin()
                CommonData.setHistoryClass(SettingsActivity::class.java as Class<T>?)
            }

            R.id.preference_activity_ll_2 ->

                goLogout("로그아웃을 하시겠습니까?")

            R.id.preference_activity_ll_3 -> {
                val layout = LayoutInflater.from(this@SettingsActivity).inflate(R.layout.open_source_dialog, null)
                val linearLayout = layout.findViewById<LinearLayout>(R.id.open_source_ll)

                MaterialDailogUtil.customDialog(
                        this@SettingsActivity,
                        linearLayout ,
                        selectListner = object : MaterialDailogUtil.OnDialogSelectListner {
                            override fun onSelect(s: String) {

                            }
                        }
                )
            }

            R.id.preference_activity_ll_4 -> {

                if (CommonData.getMemberShipType() != UserType.SUPER_ADMIN) {
                    SuperToastUtil.toastE(this@SettingsActivity, "권한이 없습니다.")
                    return
                }

                CommonData.setViewMode(ViewMode.ADD_ACOUNT_SUB_ADMIN)
                goJoin()
                CommonData.setHistoryClass(SettingsActivity::class.java as Class<T>?)
            }

            R.id.preference_activity_ll_5 -> {

                if (CommonData.getMemberShipType() != UserType.SUPER_ADMIN) {
                    SuperToastUtil.toastE(this@SettingsActivity, "권한이 없습니다.")
                    return
                }

                if (CommonData.getHolyModel() == null) {
                    Toast.makeText(this, "교회/단체 설정을 먼저 해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }

                val intent = Intent(this@SettingsActivity, MemberShipRecyclerViewActivity::class.java)
                intent.putExtra("type", UserType.SUB_ADMIN.toString())
                startActivity(intent)
                finish()
            }


            R.id.preference_activity_ll_6 -> {

                if (CommonData.getMemberShipType() != UserType.SUPER_ADMIN) {
                    SuperToastUtil.toastE(this@SettingsActivity, "권한이 없습니다.")
                    return
                }

                if (CommonData.getHolyModel() == null) {
                    Toast.makeText(this, "교회/단체 설정을 먼저 해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }
                val intent2 = Intent(this@SettingsActivity, MemberShipRecyclerViewActivity::class.java)
                intent2.putExtra("type", UserType.PERSONAL.toString())
                startActivity(intent2)
                finish()
            }
            R.id.preference_activity_ll_7 -> {

                if (CommonData.getMemberShipType() != UserType.SUPER_ADMIN) {
                    SuperToastUtil.toastE(this@SettingsActivity, "권한이 없습니다.")
                    return
                }

                simpleYesNoDialog(
                        this@SettingsActivity,
                        "저장하시겠습니까?",
                        object : MaterialDailogUtil.OnDialogSelectListner {
                            override fun onSelect(s: String) {
                                PoiUtil.saveAndShareMembers(this@SettingsActivity) { SuperToastUtil.toastE(this@SettingsActivity, " 저장이 완료되었습니다.") }
                            }
                        }
                )
            }

            R.id.preference_activity_ll_8 -> {

                if (CommonData.getMemberShipType() != UserType.SUPER_ADMIN) {
                    SuperToastUtil.toastE(this@SettingsActivity, "권한이 없습니다.")
                    return
                }


                simpleYesNoDialog(
                        this@SettingsActivity,
                        "불러오시겠습니까?",
                        object : MaterialDailogUtil.OnDialogSelectListner {
                            override fun onSelect(s: String) {
                                AppUtil.checkAppPermission(this@SettingsActivity)

                                LoggerHelper.d("Environment.getExternalStorageDirectory() : " + Environment.getExternalStorageDirectory())
                                val intent1 = Intent()
                                        .setType("*/*")
                                        .setAction(Intent.ACTION_GET_CONTENT)

                                startActivityForResult(Intent.createChooser(intent1, "Select a file"), 123)
                            }
                        }
                )
            }

            R.id.preference_activity_ll_9 ->    popToast("준비중입니다.")
            else -> {
            }
        }//if (!readyToPurchase) return;
        //bp.subscribe(SettingsActivity.this, CommonString.SUB_ADMIN_SUBSCRIBE_01);
    }

    fun setUploadCompleteExcel(tempMemberList: ArrayList<HolyModel.memberModel>) {
        FireStoreMemberManager.multiInsert(tempMemberList, DataTypeListener.OnCompleteListener {
            FDDatabaseHelper.getAllcorpsMembers(SimpleListener.OnCompleteListener {
                if (missList!!.size > 0) {
                    var strReason = ""
                    for (eleReason in missList!!) {
                        strReason += eleReason + "\n"
                    }
                    MaterialDailogUtil.simpleDoneDialog(
                            this@SettingsActivity,
                            "업로드 실패한 명단",
                            strReason ,
                            selectListner = object : MaterialDailogUtil.OnDialogSelectListner {
                                override fun onSelect(s: String) {

                                }
                            })
                }
            })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!bp!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            simpleYesNoDialog(
                    this@SettingsActivity,
                    "서버에 저장 하시겠습니까?",
                    selectListner = object : MaterialDailogUtil.OnDialogSelectListner {
                override fun onSelect(s: String) {
                    super@SettingsActivity.showProgressDialog(true)
                    //val runner = AsyncTaskRunner()
                    //runner.execute()
                    ExcelData = data
                    setGroupCoroutineExcelData()
                }
            })
        }
    }

    var membersList: ArrayList<HolyModel.memberModel> = arrayListOf()

    //엑셀에서 받은 멤버 데이터를 groupName 을 키 값으로 그룹핑한다.
    lateinit var memberToGroupMap: HashMap<String, List<HolyModel.memberModel>>

    private fun sendToServerFromEcxelData(data: Intent) {
        val selectedfile = data.data //The uri with the location of the file
        membersList = PoiUtil.readAndShareMembers(this@SettingsActivity, selectedfile!!) as ArrayList<HolyModel.memberModel>

        memberToGroupMap = membersList.groupBy { it.groupName } as HashMap

        val maxCnt = membersList.size

        if (maxCnt > 100) {
            Toast.makeText(this@SettingsActivity,
                    "서버안정화로 현재 한번에 100명 이하로 제한합니다. 등록이 필요시 개발자에게 문의하세요.",
                    Toast.LENGTH_SHORT).show()
            return
        }
    }


    //서버에 그룹을 세팅한다.
    private fun setAddGroupFromExcel() {
        LoggerHelper.d("setGroupCoroutineExcelData setAddGroupFromExcel ")
        membersList.sortBy { it.groupName }

        val curGroupMap = CommonData.getGroupMap() ?: hashMapOf()

        val colRef = FireStoreWriteManager.firestore
                .collection(FDDatabaseHelper.CORPS_TABLE)
                .document(CommonData.getHolyModel().uid)
                .collection(FDDatabaseHelper.GROUP_TABLE)

        var tempGroupList: ArrayList<HolyModel.groupModel> = arrayListOf()

        for ((key, eleGroup) in memberToGroupMap) {
            if (curGroupMap.count { it.value.name == key } <= 0) {
                var gModel = HolyModel.groupModel()
                gModel.name = key
                tempGroupList.add(gModel)
            }
        }

        FireStoreWriteManager.groupMultiInsert(tempGroupList, colRef, DataTypeListener.OnCompleteListener { t ->
            LoggerHelper.d("FireStoreWriteManager.groupMultiInsert : " + t)
            super@SettingsActivity.showProgressDialog(false)
        })
    }


    //서버에 팀을 세팅한다.
    private fun setAddTeamFromExcel() {

        LoggerHelper.d("setGroupCoroutineExcelData setAddTeamFromExcel ")

        super@SettingsActivity.showProgressDialog(true)

        membersList.sortBy { it.teamName }

        val curGroupMap = CommonData.getGroupMap() ?: hashMapOf()
        val groupList = curGroupMap.map { it.value }

        LoggerHelper.d("curGroupMap.size : " + curGroupMap.size)
        LoggerHelper.d("curGroupMap : " + curGroupMap)

        val curTeamMap = CommonData.getTeamMap() ?: hashMapOf()

        LoggerHelper.d("curTeamMap.size : " + curTeamMap.size)
        LoggerHelper.d("curTeamMap : " + curTeamMap)

        var curTeamName = "new"
        var oldteamName = "old"

        val colRef = FireStoreWriteManager.firestore
                .collection(FDDatabaseHelper.CORPS_TABLE)
                .document(CommonData.getHolyModel().uid)
                .collection(FDDatabaseHelper.TEAM_TABLE)

        val tempTeamList = arrayListOf<HolyModel.groupModel.teamModel>()
        val groupMap = CommonData.getGroupMap()

        for (eleMember in membersList) {
            eleMember.groupUID = groupMap.values.find { it.name == eleMember.groupName }?.uid
        }

        var memberToGroupUidMap = membersList.groupBy { it.groupUID } as HashMap

        for ((groupUid, memberModels) in memberToGroupUidMap) {
            val inMemberToTeamMap = memberModels.groupBy { it.teamName } as HashMap
            for ((key, eleTeam) in inMemberToTeamMap) {
                if (curTeamMap.count { it.value.name == key } > 0
                        && curTeamMap.count { it.value.groupUid == groupUid } > 0
                ) {

                } else {
                    var tModel = HolyModel.groupModel.teamModel()
                    tModel.name = key
                    tModel.groupUid = groupUid
                    tempTeamList.add(tModel)
                }
            }
        }

        FireStoreWriteManager.teamMultiInsert(tempTeamList, colRef, DataTypeListener.OnCompleteListener { t ->
            LoggerHelper.d("FireStoreWriteManager.teamMultiInsert : " + t)
            super@SettingsActivity.showProgressDialog(false)
        })
    }

    //서버에 멤버 추가한다.
    private fun setAddMemberFromExcel() {
        super@SettingsActivity.showProgressDialog(true)

        membersList.sortBy { it.name }

        val curGroupMap: HashMap<String, HolyModel.groupModel> = CommonData.getGroupMap() as HashMap<String, HolyModel.groupModel>
                ?: hashMapOf()
        val groups: ArrayList<HolyModel.groupModel> = curGroupMap.groupCovertList() as ArrayList<HolyModel.groupModel>

        LoggerHelper.d("curGroupMap.size : " + curGroupMap.size)
        LoggerHelper.d("curGroupMap : " + curGroupMap)

        val curTeamMap = CommonData.getTeamMap() ?: hashMapOf()

        LoggerHelper.d("curTeamMap.size : " + curTeamMap.size)
        LoggerHelper.d("curTeamMap : " + curTeamMap)

        var curTeamName = "new"
        var oldteamName = "old"

        val colRef = FireStoreWriteManager.firestore
                .collection(FDDatabaseHelper.CORPS_TABLE)
                .document(CommonData.getHolyModel().uid)
                .collection(FDDatabaseHelper.TEAM_TABLE)

        val tempTeamList = arrayListOf<HolyModel.groupModel.teamModel>()
        var tempTeamMap: HashMap<String, HolyModel.groupModel.teamModel> = hashMapOf()
        for (eleGroup in groups) {
            for (eleTeam in curTeamMap) {
                tempTeamMap.put(eleGroup.name + eleTeam.value.name, eleTeam.value)
            }
        }

        for (eleMember in membersList) {
            curTeamName = eleMember.groupName + eleMember.teamName
            if (curTeamName != oldteamName) {
                if (tempTeamMap[curTeamName] == null) {

                    var teamModel = HolyModel.groupModel.teamModel()
                    teamModel.name = eleMember.teamName

                    for (eleGroup in groups) {
                        if (eleMember.groupName == eleGroup.name) {
                            teamModel.groupUid = eleGroup.uid
                        }
                    }

                    oldteamName = curTeamName

                    tempTeamList.add(teamModel)
                }
            }
        }


        FireStoreWriteManager.teamMultiInsert(tempTeamList, colRef, DataTypeListener.OnCompleteListener { t ->
            LoggerHelper.d("FireStoreWriteManager.teamMultiInsert : " + t)
            super@SettingsActivity.showProgressDialog(false)
        })
    }

    private fun sendEcxelData() {

        LoggerHelper.d("setGroupCoroutineExcelData sendEcxelData ")
        val missMemberMap = HashMap<String, HolyModel.memberModel>()

        if (missList != null) {
            missList!!.clear()
            missList = null
        }
        missList = ArrayList()
        val maxCnt = membersList.size

        if (maxCnt > 100) {
            Toast.makeText(this@SettingsActivity, "서버안정화로 현재 한번에 100명 이하로 제한합니다. 등록이 필요시 개발자에게 문의하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        var tempMemberList: ArrayList<HolyModel.memberModel> = ArrayList()

        var curCnt = 0
        for (eleMember in membersList) {
            if (!FDDatabaseHelper.getCompareData(eleMember)) {
                val groupMap = CommonData.getGroupMap()
                for ((key, elemTemp) in groupMap) {
                    elemTemp.uid = key
                    val tempName = elemTemp.name

                    if (tempName == eleMember.groupName) {
                        eleMember.groupUID = elemTemp.uid
                        val teamMap = CommonData.getTeamMap()
                                .filter { it.value.groupUid == eleMember.groupUID }   //elemTemp.team ?: break

                        for ((key1, elemTeamTemp) in teamMap) {
                            elemTeamTemp.uid = key1
                            var teamTempName: String
                            var compareTeamName: String

                            try {
                                teamTempName = SortMapUtil.getInteger(elemTeamTemp.name)!!.toString()
                            } catch (e: Exception) {
                                teamTempName = elemTeamTemp.name
                            }

                            try {
                                compareTeamName = SortMapUtil.getInteger(eleMember.teamName)!!.toString()
                            } catch (e: Exception) {
                                try {
                                    compareTeamName = Common.trim(eleMember.teamName)
                                } catch (e2: Exception) {
                                    compareTeamName = ""
                                }
                            }

                            LoggerHelper.d("compareTeamName : $compareTeamName" + " / " + "teamTempName : $teamTempName")
                            if (Common.trim(teamTempName) == compareTeamName) {
                                eleMember.teamUID = elemTeamTemp.uid
                                eleMember.teamName = elemTeamTemp.name
                                LoggerHelper.d("PASS : " + curCnt + " / " + "elemTeamTemp.uid : " + elemTeamTemp.uid + " / "
                                        + "elemTeamTemp.name : " + elemTeamTemp.name +
                                        " / " + "eleMember.name : " + eleMember.name +
                                        " / " + "eleMember.teamName : " + eleMember.teamName)
                                break
                            } else {

                            }
                        }
                    }
                }

                if (eleMember.groupUID == null) {
                    LoggerHelper.d(eleMember.name + "[" + eleMember.groupUID + "] 는 존재하지 않는 " + CommonString.GROUP_NICK + "입니다")
                    missList!!.add(eleMember.name + "[" + eleMember.groupUID + "] 는 존재하지 않는 " + CommonString.GROUP_NICK + "입니다")
                } else {
                    eleMember.corpsName = CommonData.getHolyModel().name
                    eleMember.corpsUID = CommonData.getHolyModel().uid
                    curCnt++

                    tempMemberList.add(eleMember)
                    if (maxCnt == curCnt) {
                        setUploadCompleteExcel(tempMemberList)
                        Toast.makeText(this@SettingsActivity, "$maxCnt 명 업데이트가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                /*setSendToServer(
                        memberManager,
                        eleMember,
                        SimpleListener.OnCompleteListener {
                            if (maxCnt == curCnt) {
                                setUploadCompleteExcel()
                                Toast.makeText(this@SettingsActivity, "$maxCnt 명 업데이트가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                        })*/
            } else {

                sendServerFailList(eleMember, missMemberMap, curCnt, maxCnt, tempMemberList)
            }
        }
        if (missMemberMap.size > 0)
            Toast.makeText(this, missMemberMap.toString(), Toast.LENGTH_SHORT).show()
    }

    fun sendServerFailList(eleMember: HolyModel.memberModel, missMemberMap: HashMap<String, HolyModel.memberModel>, curCnt: Int, maxCnt: Int, tempMemberList: ArrayList<HolyModel.memberModel>) {
        var curCnt1 = curCnt
        LoggerHelper.d(eleMember.name + "  같은 이름이 존재합니다.")
        missMemberMap[eleMember.name] = eleMember
        missList!!.add(eleMember.name + " 같은 이름이 존재합니다.")
        curCnt1++
        if (maxCnt == curCnt1) {
            setUploadCompleteExcel(tempMemberList)
            Toast.makeText(this@SettingsActivity, "$maxCnt 명 업데이트가 완료되었습니다.", Toast.LENGTH_SHORT).show()
        }
        //return curCnt1
    }

    private fun setSendToServer(
            memberManager: MemberManager,
            memberModel: HolyModel.memberModel,
            onCompleteListener: SimpleListener.OnCompleteListener) {

        FireStoreMemberManager.insert(memberModel, DataTypeListener.OnCompleteListener {
            onCompleteListener.onComplete()
        })
    }

    private fun updateTextViews() {
        //popToast(String.format("%s is%s subscribed", CommonString.SUB_ADMIN_SUBSCRIBE_01, bp.isSubscribed(CommonString.SUB_ADMIN_SUBSCRIBE_01) ? "" : " not"));
        //SkuDetails subs = bp.getSubscriptionListingDetails(CommonString.SUB_ADMIN_SUBSCRIBE_01);
        //popToast(subs != null ? subs.toString() : "Failed to load subscription details");

        //TextView textView = findViewById(R.id.select_activity_ll9_tv2);
        //textView.setText(String.format("%s is%s subscribed", CommonString.SUB_ADMIN_SUBSCRIBE_01, bp.isSubscribed(CommonString.SUB_ADMIN_SUBSCRIBE_01) ? "" : " not"));
    }

    public override fun onDestroy() {
        if (bp != null) {
            bp!!.release()
        }
        super.onDestroy()
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        // * 구매 완료시 호출
        // productId: 구매한 sku (ex) no_ads)
        // details: 결제 관련 정보
        if (productId == CommonString.SUB_ADMIN_SUBSCRIBE_01) {
            popToast("CommonString.SUB_ADMIN_SUBSCRIBE_01 : " + "를 구매해주셔서 감사합니다.")
            //bp.consumePurchase(CommonString.SUB_ADMIN_SUBSCRIBE_01);
            CommonData.setCurrentPremiumType(PremiupType.ADS_PREMIUM)
            // * 광고 제거는 1번 구매하면 영구적으로 사용하는 것이므로 consume하지 않지만,
            // 만약 게임 아이템 100개를 주는 것이라면 아래 메소드를 실행시켜 다음번에도 구매할 수 있도록 소비처리를 해줘야한다.
            // bp.consumePurchase(Config.Sku);
        }
    }

    override fun onResume() {
        super.onResume()
        updateTextViews()
    }

    override fun onPurchaseHistoryRestored() {
        // * 구매 정보가 복원되었을때 호출
        // bp.loadOwnedPurchasesFromGoogle() 하면 호출 가능
        popToast("onPurchaseHistoryRestored")
        for (sku in bp!!.listOwnedProducts())
            LoggerHelper.d("Owned Managed Product: $sku")
        for (sku in bp!!.listOwnedSubscriptions())
            LoggerHelper.d("Owned Subscription: $sku")
        updateTextViews()
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        // * 구매 오류시 호출
        // errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED 일때는
        // 사용자가 단순히 구매 창을 닫은것임으로 이것 제외하고 핸들링하기.

        if (errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED) {
            //  popToast("BILLING_RESPONSE_RESULT_USER_CANCELED");
        } else if (errorCode == Constants.BILLING_ERROR_BIND_PLAY_STORE_FAILED) {
            // popToast("BILLING_ERROR_BIND_PLAY_STORE_FAILED");
        } else if (errorCode == Constants.BILLING_ERROR_CONSUME_FAILED) {
            //  popToast("BILLING_ERROR_CONSUME_FAILED");
        } else if (errorCode == Constants.BILLING_ERROR_FAILED_LOAD_PURCHASES) {
            //  popToast("BILLING_ERROR_FAILED_LOAD_PURCHASES");
        } else if (errorCode == Constants.BILLING_ERROR_FAILED_TO_INITIALIZE_PURCHASE) {
            // popToast("BILLING_ERROR_FAILED_TO_INITIALIZE_PURCHASE");
        } else if (errorCode == Constants.BILLING_ERROR_OTHER_ERROR) {
            // popToast("BILLING_ERROR_OTHER_ERROR");
        } else if (errorCode == Constants.BILLING_ERROR_INVALID_SIGNATURE) {
            //popToast("BILLING_ERROR_INVALID_SIGNATURE");
        } else if (errorCode == Constants.BILLING_ERROR_INVALID_MERCHANT_ID) {
            //popToast("BILLING_ERROR_INVALID_MERCHANT_ID");
        } else if (errorCode == Constants.BILLING_ERROR_LOST_CONTEXT) {
            //popToast("BILLING_ERROR_LOST_CONTEXT");
        } else if (errorCode == Constants.BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE) {
            //popToast("BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE");
        } else if (errorCode == Constants.BILLING_ERROR_SKUDETAILS_FAILED) {
            //popToast("BILLING_ERROR_SKUDETAILS_FAILED");
        } else {
            //popToast(String.valueOf(errorCode));
        }
    }

    override fun onBillingInitialized() {
        // * 처음에 초기화됬을때.
        // storage에 구매여부 저장
        //popToast("onBillingInitialized");
        readyToPurchase = true
        updateTextViews()
    }

    fun setGroupCoroutineExcelData() {
        runBlocking<Unit> {
            sendToServerFromEcxelData(ExcelData!!)
            setAddGroupFromExcel()

            FDDatabaseHelper.getGroupDataToStore(DataTypeListener.OnCompleteListener {
                LoggerHelper.d("FDDatabaseHelper.getGroupDataToStore  OnCompleteListener")
                val gMap: HashMap<String, HolyModel.groupModel> = CommonData.getGroupMap()
                        as HashMap<String, HolyModel.groupModel>
                for ((key, value) in gMap) {

                    LoggerHelper.d("getGroupDataToStore.key : $key  / value : $value")

                    value.uid = key
                }

                setTeamCoroutineExcelData()
            })
        }

    }

    fun setTeamCoroutineExcelData() {

        runBlocking<Unit> {
            setAddTeamFromExcel()
            FDDatabaseHelper.getTeamAllDataToStore(DataTypeListener.OnCompleteListener {
                LoggerHelper.d("FDDatabaseHelper.getTeamDataToStore  OnCompleteListener!!!!")
                val tMap: HashMap<String, HolyModel.groupModel.teamModel> = CommonData.getTeamMap()
                        as HashMap<String, HolyModel.groupModel.teamModel>
                for ((key, value) in tMap) {

                    LoggerHelper.d("getTeamDataToStore.key : $key  / value : $value")
                    value.uid = key
                }

                sendEcxelData()
            })
        }
    }
}
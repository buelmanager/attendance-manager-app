package com.buel.holyhelper.view.activity

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
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
import com.buel.holyhelper.view.DataTypeListener
import com.buel.holyhelper.view.SimpleListener
import com.buel.holyhelper.view.recyclerView.memberShipRecyclerView.MemberShipRecyclerViewActivity
import com.commonLib.Common
import com.commonLib.MaterialDailogUtil
import com.commonLib.SuperToastUtil
import com.orhanobut.logger.LoggerHelper
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
                        linearLayout
                ) { s ->

                }
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


                MaterialDailogUtil.simpleYesNoDialog(
                        this@SettingsActivity,
                        "저장하시겠습니까?"
                ) { s ->
                    PoiUtil.saveAndShareMembers(this@SettingsActivity) { SuperToastUtil.toastE(this@SettingsActivity, " 저장이 완료되었습니다.") }
                }
            }

            R.id.preference_activity_ll_8 -> {

                if (CommonData.getMemberShipType() != UserType.SUPER_ADMIN) {
                    SuperToastUtil.toastE(this@SettingsActivity, "권한이 없습니다.")
                    return
                }


                MaterialDailogUtil.simpleYesNoDialog(
                        this@SettingsActivity,
                        "불러오시겠습니까?"
                ) { s ->

                    AppUtil.checkAppPermission(this@SettingsActivity)

                    LoggerHelper.d("Environment.getExternalStorageDirectory() : " + Environment.getExternalStorageDirectory())
                    val intent1 = Intent()
                            .setType("*/*")
                            .setAction(Intent.ACTION_GET_CONTENT)

                    startActivityForResult(Intent.createChooser(intent1, "Select a file"), 123)

                }
            }
            R.id.preference_activity_ll_9 ->

                popToast("준비중입니다.")
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
                    MaterialDailogUtil.simpleDoneDialog(this@SettingsActivity, "업로드 실패한 명단", strReason) {
                        //goMain();
                    }
                }
            })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!bp!!.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            MaterialDailogUtil.simpleYesNoDialog(
                    this@SettingsActivity,
                    "서버에 저장 하시겠습니까?"
            ) { s ->
                super@SettingsActivity.showProgressDialog(true)
                val runner = AsyncTaskRunner()
                runner.execute()
                ExcelData = data
            }
        }

    }

    private fun sendToServerFromEcxelData(data: Intent) {
        val selectedfile = data.data //The uri with the location of the file
        val membersList = PoiUtil.readAndShareMembers(this@SettingsActivity, selectedfile!!)
        val maxCnt = membersList.size

        if (maxCnt > 100) {
            Toast.makeText(this@SettingsActivity, "서버안정화로 현재 한번에 100명 이하로 제한합니다. 등록이 필요시 개발자에게 문의하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        setAddGroupFromExcel(membersList as ArrayList<HolyModel.memberModel>)
    }

    //서버에 그룹을 세팅한다.
    private fun setAddGroupFromExcel(membersList: ArrayList<HolyModel.memberModel>) {

        membersList.sortBy { it.groupName }

        val curGroupMap = CommonData.getHolyModel().group
        LoggerHelper.d("curGroupMap.size : " + curGroupMap.size)
        LoggerHelper.d("curGroupMap : " + curGroupMap)
        var tempGroupMap: HashMap<String, HolyModel.memberModel> = hashMapOf()
        var tempGroupList: ArrayList<HolyModel.groupModel> = arrayListOf()
        var curGroupName = "new"
        var oldGroupName = "old"
        for (elemember in membersList) {

            curGroupName = elemember.groupName
            if (!curGroupName.equals(oldGroupName)) {

                if (curGroupMap.filter { it.value.name == elemember.groupName }.isEmpty()) {
                    var gModel = HolyModel.groupModel()
                    gModel.name = elemember.groupName

                    oldGroupName = curGroupName
                    LoggerHelper.d("gModel.name : " + gModel.name)

                    tempGroupList.add(gModel)
                }
            }
        }

        val docRef = FireStoreWriteManager.firestore
                .collection(FDDatabaseHelper.CORPS_TABLE)
                .document(CommonData.getHolyModel().uid)
                .collection(FDDatabaseHelper.GROUP_TABLE)
                .document()

        FireStoreWriteManager.multiInsert(tempGroupList, docRef, DataTypeListener.OnCompleteListener { t ->

            LoggerHelper.d("FireStoreWriteManager.multiInsert : " + t)

        })
    }

    private fun sendEcxelData(data: Intent) {
        val selectedfile = data.data //The uri with the location of the file
        val membersList = PoiUtil.readAndShareMembers(this@SettingsActivity, selectedfile!!)

        val memberManager = MemberManager()
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
                val groupMap = CommonData.getHolyModel().group
                for ((key, elemTemp) in groupMap) {
                    elemTemp.uid = key
                    val tempName = elemTemp.name

                    if (tempName == eleMember.groupName) {
                        eleMember.groupUID = elemTemp.uid
                        val teamMap = elemTemp.team ?: break

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

    private inner class AsyncTaskRunner : AsyncTask<String, String, String>() {

        private var resp: String? = null

        override fun doInBackground(vararg params: String): String? {
            publishProgress("Sleeping...") // Calls onProgressUpdate()
            try {
                sendToServerFromEcxelData(ExcelData!!)
            } catch (e: Exception) {
                e.printStackTrace()
                resp = e.message
            }
            return resp
        }

        override fun onPostExecute(result: String) {
            super@SettingsActivity.showProgressDialog(false)
        }

        override fun onPreExecute() {}


        override fun onProgressUpdate(vararg text: String) {}
    }
}
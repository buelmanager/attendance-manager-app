package com.buel.holyhelpers.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.AdminMode
import com.buel.holyhelpers.data.CommonData
import com.buel.holyhelpers.data.CommonString
import com.buel.holyhelpers.data.FDDatabaseHelper.getAllcorpsMembers
import com.buel.holyhelpers.data.FDDatabaseHelper.getGroupModelFromMap
import com.buel.holyhelpers.data.FDDatabaseHelper.setUploadImageSendMemberStorage
import com.buel.holyhelpers.management.Management
import com.buel.holyhelpers.management.MemberManager
import com.buel.holyhelpers.model.HolyModel.memberModel
import com.buel.holyhelpers.utils.SortMapUtil
import com.buel.holyhelpers.view.DataReturnListener
import com.buel.holyhelpers.view.SimpleListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.commonLib.Common
import com.commonLib.MaterialDailogUtil.Companion.datePickerDialog2
import com.commonLib.MaterialDailogUtil.Companion.getExcutive
import com.commonLib.MaterialDailogUtil.Companion.getGender
import com.commonLib.MaterialDailogUtil.Companion.getNew
import com.commonLib.MaterialDailogUtil.Companion.getPosition
import com.commonLib.MaterialDailogUtil.Companion.getTeamDialog
import com.commonLib.MaterialDailogUtil.Companion.simpleDoneDialog
import com.commonLib.MaterialDailogUtil.Companion.simpleListDialog
import com.commonLib.MaterialDailogUtil.OnDialogSelectListner
import com.commonLib.SuperToastUtil
import com.orhanobut.logger.LoggerHelper
import kotlinx.android.synthetic.main.activity_memeber_view.*
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*
import android.provider.ContactsContract.CommonDataKinds

class MemberManagerViewActivity : BaseActivity(), View.OnClickListener {

    private var mImageUri: Uri? = null
    private var oldImageUri: String? = null
    private var mTell: String? = null
    private var mBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memeber_view)
        val titleView = findViewById<TextView>(R.id.member_manager_view_main_tv_desc)
        if (CommonData.getHolyModel() == null) {
            Toast.makeText(this, CommonString.INFO_TITLE_CONTROL_CORP, Toast.LENGTH_SHORT).show()
            goSelect()
            return
        }
        //그룹 리스트가 없는경우, 그룹선택이 안되어 있는경우
        if (CommonData.getHolyModel().group == null || CommonData.getGroupModel() == null) {
            Toast.makeText(this, CommonString.INFO_TITLE_CONTROL_GROUP, Toast.LENGTH_SHORT).show()
            goSelect()
            return
        }

        member_activity_view_imageivew_profile.setOnClickListener(this)
        member_activity_view_et_add.setOnClickListener(this)
        //etAdd2.setVisibility(View.INVISIBLE);

        //AppUtil.setBackColor(MemberManagerViewActivity.this, btnCall, R.color.vordiplom_color_green);
        member_activity_view_btn_search.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.data = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            startActivityForResult(intent, PIC_FROM_ADDRESS)
        }
        setFocusEditText(member_activity_view_et_name)
        setFocusEditText(member_activity_view_et_phone)
        setFocusEditText(member_activity_view_et_add)
        setFocusEditText(member_activity_view_et_gender)
        setFocusEditText(member_activity_view_et_leader)
        setFocusEditText(member_activity_view_et_position)
        setFocusEditText(member_activity_view_et_new)
        setFocusEditText(member_activity_view_et_isexecutives)
        setFocusEditText(member_activity_view_et_birth)
        CommonData.setSelectedGroup(CommonData.getGroupModel())
        CommonData.setSelectedTeam(CommonData.getTeamModel())
        if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            val member = CommonData.getSelectedMember()
            member_activity_view_et_name.setText(member.name)
            member_activity_view_et_phone.setText(member.phone)
            member_activity_view_et_add.setText(member.address)
            member_activity_view_et_add2.setText(member.town)
            member_activity_view_et_gender.setText(member.gender)
            member_activity_view_et_leader.setText(member.leader)
            member_activity_view_et_position.setText(member.position)
            member_activity_view_et_group.setText(CommonData.getGroupName(member.groupUID))
            try {
                member_activity_view_et_team.setText(SortMapUtil.getInteger(CommonData.getTeamName(member.teamUID)).toString())
            } catch (e: Exception) {
                member_activity_view_et_team.setText(member.teamName)
            }
            if (member.groupUID != CommonData.getGroupModel().uid) {
                val group = getGroupModelFromMap(CommonData.getHolyModel().group, member.groupUID.toString())
                if (group.name == null) {
                    popToast("해당부서가 없습니다. 같은 이름으로 생성해주세요.")
                    goSelect()
                    return
                }
                CommonData.setSelectedGroup(group)
                if (member.teamName != null && member.teamUID != null) member_activity_view_et_team.setText(member.teamName)
                popToast(group.name + " 으로 부서를 설정하였습니다.")
            }
            member_activity_view_et_new.setText(member.isNew)
            member_activity_view_et_new.setText(member.isNew)
            member_activity_view_et_isexecutives.setText(member.isExecutives)
            member_activity_view_et_birth.setText(member.birth)
            oldImageUri = member.userPhotoUri
            if (member.userPhotoUri == null) {
                try {
                    Glide.with(this@MemberManagerViewActivity)
                            .load(R.drawable.ic_account)
                            .apply(RequestOptions().circleCrop())
                            .into(member_activity_view_imageivew_profile)
                } catch (e: Exception) {
                    LoggerHelper.e(e.message)
                }
            } else {
                Glide.with(this@MemberManagerViewActivity)
                        .load(member.userPhotoUri)
                        .apply(RequestOptions().circleCrop())
                        .into(member_activity_view_imageivew_profile)
            }
        } else if (CommonData.getAdminMode() == AdminMode.NORMAL) {
            member_activity_view_et_isexecutives.setText("회원/성도")
            if (CommonData.getGroupModel() != null) member_activity_view_et_group.setText(CommonData.getGroupModel().name)
            if (CommonData.getTeamModel() != null) {
                LoggerHelper.d("CommonData.getTeamModel() : " + CommonData.getTeamModel())
                LoggerHelper.d("CommonData.getTeamModel() toString  : " + CommonData.getTeamModel().toString())
                member_activity_view_et_team.setText(SortMapUtil.getInteger(CommonData.getTeamModel().name).toString() + "  " + CommonData.getTeamModel().etc)
            }
            member_activity_view_btn_call.visibility = View.INVISIBLE
        }
        member_activity_view_et_name.setOnClickListener(this)
        member_activity_view_et_birth.setOnClickListener(this)
        member_activity_view_et_group.setOnClickListener(this)
        member_activity_view_et_team.setOnClickListener(this)
        member_activity_view_et_gender.setOnClickListener(this)
        member_activity_view_et_position.setOnClickListener(this)
        member_activity_view_et_new.setOnClickListener(this)
        member_activity_view_et_isexecutives.setOnClickListener(this)
        member_activity_view_btn_call.setOnClickListener(this)
        member_activity_view_btn_group.setOnClickListener(this)
        member_activity_view_btn_team.setOnClickListener(this)
        member_activity_view_btn_gender.setOnClickListener(this)
        member_activity_view_btn_position.setOnClickListener(this)
        member_activity_view_btn_birth.setOnClickListener(this)
        member_activity_view_btn_new.setOnClickListener(this)
        member_activity_view_btn_isexecutives.setOnClickListener(this)
        setTopLayout(this)
        super.setTopTitleDesc("성도/회원 수정/관리 페이지")
    }

    var isFstMember = false
    @SuppressLint("LongLogTag")
    private fun sendMemberData() {
        val memberManager = MemberManager()
        val memberModel = memberModel()
        val membersArrayList: ArrayList<memberModel> = getServerData
        val isCompare = getCompareData(member_activity_view_et_name!!.text.toString(), membersArrayList)
        if (member_activity_view_et_name!!.text.toString() == "") {
            SuperToastUtil.toast(this, "이름을 입력해주세요.")
            isSaving = false
            return
        }
        if (member_activity_view_et_new!!.text.toString() == "") {
            SuperToastUtil.toast(this, "등록구분을 입력해주세요.")
            isSaving = false
            return
        }
        if (member_activity_view_et_phone!!.text.toString() == "" || member_activity_view_et_gender!!.text.toString() == "" || member_activity_view_et_leader!!.text.toString() == "" || member_activity_view_et_birth!!.text.toString() == "") {
            SuperToastUtil.toast(this, "전화/생년월일/성별/인도자를  입력해주세요.")
            isSaving = false
            return
        }
        if (CommonData.getSelectedGroup() == null || CommonData.getSelectedTeam() == null) {
            SuperToastUtil.toast(this, "선택된 " + CommonString.GROUP_NICK + " 또는 " + CommonString.TEAM_NICK + "이/가 없습니다.")
            isSaving = false
            return
        }
        if (isCompare) {
            Log.d(TAG, "sendServer: isCompare = $isCompare")
            SuperToastUtil.toast(this, "같은 이름이 있습니다.")
            isSaving = false
            return
        }
        memberModel.name = member_activity_view_et_name!!.text.toString()
        memberModel.phone = member_activity_view_et_phone!!.text.toString()
        memberModel.address = member_activity_view_et_add!!.text.toString()
        memberModel.town = member_activity_view_et_add2!!.text.toString()
        memberModel.gender = member_activity_view_et_gender!!.text.toString()
        memberModel.leader = member_activity_view_et_leader!!.text.toString()
        memberModel.position = member_activity_view_et_position!!.text.toString()
        memberModel.corpsUID = CommonData.getCorpsUid()
        memberModel.corpsName = CommonData.getHolyModel().name
        memberModel.groupName = CommonData.getSelectedGroup().name
        memberModel.teamName = CommonData.getSelectedTeam().name
        memberModel.groupUID = CommonData.getSelectedGroup().uid
        memberModel.teamUID = CommonData.getSelectedTeam().uid
        memberModel.isNew = member_activity_view_et_new!!.text.toString()
        memberModel.isExecutives = member_activity_view_et_isexecutives!!.text.toString()
        memberModel.birth = member_activity_view_et_birth!!.text.toString()
        if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            if (mImageUri == null) {
                memberModel.userPhotoUri = oldImageUri
            }
        }
        LoggerHelper.d("MemberManagerViewAcitivity", memberModel.toString())
        LoggerHelper.d("MemberManagerViewAcitivity", "CommonData.getViewMode() = " + CommonData.getViewMode())
        if (isFstMember) {
            memberModel.memberRegistDate = Common.currentTimestamp()
        }
        setSendToServer(memberManager, memberModel, SimpleListener.OnCompleteListener {
            if (mImageUri == null) {
                if (CommonData.isTutoMode()) {
                    simpleDoneDialog(this@MemberManagerViewActivity,
                            "#5 단계, 축하합니다. 이제 출석체크하러 갑니다!", object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            goMain()
                        }
                    })
                } else { //CommonData.setViewMode(ViewMode.ADMIN);
//goMemberRecycler();
                    finish()
                    isSaving = false
                }
                return@OnCompleteListener
            }
            showProgressDialog(true)
            setUploadImageSendMemberStorage(uploadmember!!, mBitmap!!, DataReturnListener.OnCompleteListener { str ->
                uploadmember!!.userPhotoUri = str
                memberManager.modify(uploadmember, object : Management.OnCompleteListener<Any?> {
                    @SuppressLint("LongLogTag")
                    override fun onComplete(data: Any?) {
                        Log.d(TAG, "MODIFY onComplete: memberManager insert complete!!!")
                        getAllcorpsMembers(SimpleListener.OnCompleteListener {
                            showProgressDialog(false)
                            if (CommonData.isTutoMode()) {
                                simpleDoneDialog(this@MemberManagerViewActivity,
                                        "#5 단계, 축하합니다. 이제 출석체크하러 갑니다!", object : OnDialogSelectListner {
                                    override fun onSelect(s: String) {
                                        goMain()
                                    }
                                })
                            } else {
                                isSaving = false
                                finish()
                            }
                        })
                    }
                })
            })
        })
    }

    private var uploadmember: memberModel? = null
    @SuppressLint("LongLogTag")
    private fun setSendToServer(memberManager: MemberManager, memberModel: memberModel, onCompleteListener: SimpleListener.OnCompleteListener) {
        if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            memberModel.uid = CommonData.getSelectedMember().uid
            memberManager.modify(memberModel) { data ->
                Log.d(TAG, "MODIFY onComplete: memberManager insert complete!!!")
                getAllcorpsMembers(SimpleListener.OnCompleteListener {
                    val membersArrayList: ArrayList<memberModel> = getServerData
                    val isCompare = getCompareData(member_activity_view_et_name!!.text.toString(), membersArrayList)
                    uploadmember = getSerchMember(memberModel, membersArrayList)
                    onCompleteListener.onComplete()
                })
            }
        } else if (CommonData.getAdminMode() == AdminMode.NORMAL) {
            memberManager.insert(memberModel) { data ->
                Log.d(TAG, "ADMIN onComplete: memberManager insert complete!!!")
                getAllcorpsMembers(SimpleListener.OnCompleteListener {
                    val membersArrayList: ArrayList<memberModel> = getServerData
                    val isCompare = getCompareData(member_activity_view_et_name!!.text.toString(), membersArrayList)
                    uploadmember = getSerchMember(memberModel, membersArrayList)
                    onCompleteListener.onComplete()
                })
            }
        }
    }//for (Map.Entry<String, HolyModel.memberModel> elem : CommonData.getHolyModel().memberModel.entrySet()) {

    /**
     * 멤버 리스트를 가지고 온다.
     *
     * @return
     */
    val getServerData: ArrayList<memberModel>
        get() {
            val members = ArrayList<memberModel>()
            try {
                LoggerHelper.d("CommonData.getMembersMap() size : +  " + CommonData.getMembersMap().size)
                //for (Map.Entry<String, HolyModel.memberModel> elem : CommonData.getHolyModel().memberModel.entrySet()) {
                for ((key, eleGroup) in CommonData.getMembersMap()) {
                    eleGroup.uid = key
                    members.add(eleGroup)
                }
            } catch (e: Exception) {
            }
            return members
        }

    /**
     * 데이터를 비교한다.
     *
     * @param strComare
     * @param members
     * @return
     */
    fun getCompareData(
            strComare: String,
            members: ArrayList<memberModel>): Boolean {
        if (CommonData.getAdminMode() == AdminMode.MODIFY && CommonData.getSelectedMember().name == strComare) { //if (CommonData.getSelectedGroup().name.equals(strComare)) {
            return false
            //}
        }
        if (strComare == "") {
            SuperToastUtil.toast(this@MemberManagerViewActivity, "이름을 입력해주세요.")
            return true
        }
        //단체에서
        for (eleMember in members) { //그룹이 같고
            LoggerHelper.d(" eleMember.groupUID : " + eleMember.groupUID)
            LoggerHelper.d(" CommonData.getGroupModel().uid : " + CommonData.getGroupModel().uid)
            if (eleMember.groupUID != null) {
                if (eleMember.groupUID == CommonData.getGroupModel().uid) { //이름이 같은것을 검색
                    if (eleMember.name == strComare) {
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * 데이터를 비교한다.
     * 현재 모델과 비교하여서 같은이름의  uid를 받아온다
     *
     * @param cMember
     * @param members
     * @return
     */
    fun getSerchMember(
            cMember: memberModel,
            members: ArrayList<memberModel>): memberModel? {
        var searchMember: memberModel? = null
        for (eleMember in members) {
            if (eleMember.name == cMember.name
                    && eleMember.groupUID == cMember.groupUID) {
                searchMember = eleMember
                return searchMember
            }
        }
        return searchMember
    }

    private fun onProfileClick() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PIC_FROM_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PIC_FROM_ADDRESS && resultCode == Activity.RESULT_OK) {
            if (data == null) return
            val cursor = contentResolver.query(data.data, arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER), null, null, null)
            cursor.moveToFirst()

            val name = cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME))
            val number = cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER))

            member_activity_view_et_name.setText(name)
            member_activity_view_et_phone.setText(number)

            cursor.close()
        }
        if (requestCode == PIC_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            if (data == null) return
            LoggerHelper.d("requestCode : $requestCode")
            LoggerHelper.d("resultCode : $resultCode")
            LoggerHelper.d("data : " + data.data)
            mImageUri = data.data
            member_activity_view_imageivew_profile!!.setImageURI(mImageUri)
            val imgView = member_activity_view_imageivew_profile
            try {
                mBitmap = Common.getBitmapFromUri(this@MemberManagerViewActivity, mImageUri)
                imgView!!.setImageBitmap(mBitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            LoggerHelper.d("이미지 추가")
        } else if (requestCode == DAUM_SEARCH_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getStringExtra("result")
                member_activity_view_et_add!!.setText(result)
                member_activity_view_et_add2!!.visibility = View.VISIBLE
            }
            if (resultCode == Activity.RESULT_CANCELED) { //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.member_activity_view_et_add -> {
            }
            R.id.member_activity_view_et_position, R.id.member_activity_view_btn_position -> simpleListDialog(
                    this@MemberManagerViewActivity,
                    R.array.position_option,
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            member_activity_view_et_position!!.setText(getPosition(Integer.valueOf(s)))
                        }
                    })
            R.id.member_activity_view_btn_birth, R.id.member_activity_view_et_birth -> datePickerDialog2(
                    this@MemberManagerViewActivity,
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            member_activity_view_et_birth!!.setText(s)
                        }
                    }
            )
            R.id.member_activity_view_btn_isexecutives, R.id.member_activity_view_et_isexecutives -> simpleListDialog(
                    this@MemberManagerViewActivity,
                    R.array.new_excutive,
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            member_activity_view_et_isexecutives!!.setText(getExcutive(Integer.valueOf(s)))
                        }
                    })
            R.id.member_activity_view_btn_new, R.id.member_activity_view_et_new -> simpleListDialog(
                    this@MemberManagerViewActivity,
                    R.array.new_option,
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            isFstMember = if (s == "0") true else false
                            isFstMember = if (s == "0") false else true
                            member_activity_view_et_new!!.setText(getNew(Integer.valueOf(s)))
                        }
                    })
            R.id.member_activity_view_btn_gender, R.id.member_activity_view_et_gender -> simpleListDialog(
                    this@MemberManagerViewActivity,
                    R.array.gender_option,
                    object : OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            member_activity_view_et_gender!!.setText(getGender(Integer.valueOf(s)))
                        }
                    })
            R.id.member_activity_view_btn_group, R.id.member_activity_view_et_group -> {
                Common.hideKeyboard(v.context as Activity)

                //@Deprecated : 그룹이동 불가능 하도록 수정
                /*
                getGroupDialog(v.context,
                        SimpleListener.OnCompleteListener {
                            member_activity_view_et_group!!.setText(CommonData.getSelectedGroup().name)
                            CommonData.setSelectedTeam(null)
                            member_activity_view_et_team!!.setText("")
                        })

                 */
            }
            R.id.member_activity_view_et_team, R.id.member_activity_view_btn_team -> {
                Common.hideKeyboard(v.context as Activity)
                getTeamDialog(v.context,
                        SimpleListener.OnCompleteListener {
                            LoggerHelper.d("CommonData.getSelectedTeam().name : " + CommonData.getSelectedTeam().name)
                            member_activity_view_et_team!!.setText(CommonData.getSelectedTeam().name + "  " + CommonData.getSelectedTeam().etc + "  ")
                        })
            }
            R.id.top_bar_btn_ok -> {
                Common.hideKeyboard(this@MemberManagerViewActivity)
                if (member_activity_view_et_team!!.text.toString() == null) {
                    SuperToastUtil.toastE(this@MemberManagerViewActivity,
                            CommonString.INFO_TITLE_CONTROL_TEAM_OR_GROUP)
                    isSaving = false
                    return
                }
                if (!isSaving) {
                    isSaving = true
                    sendMemberData()
                } else {
                    SuperToastUtil.toastE(this, "저장중입니다. 잠시만 기다리세요.")
                }
            }
            R.id.member_activity_view_btn_call -> {
                mTell = member_activity_view_et_phone!!.text.toString().replace("-", "")
                //AppUtil.sendCall(MemberManagerViewActivity.this, strTell, true);
                Common.sendDirectCall(mTell, this)
            }
            R.id.top_bar_btn_back -> finish()
            R.id.member_activity_view_imageivew_profile -> onProfileClick()
            else -> {
            }
        }
    }

    /**
     * 권한 요청에 대한 응답을 이곳에서 가져온다.
     *
     * @param requestCode 요청코드 * @param permissions 사용자가 요청한 권한들 * @param grantResults 권한에 대한 응답들(인덱스별로 매칭)
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1000) { // 요청한 권한을 사용자가 "허용" 했다면...
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$mTell"))
                // Add Check Permission
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this@MemberManagerViewActivity, "권한요청을 거부했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PIC_FROM_ALBUM = 10
        private const val PIC_FROM_ADDRESS = 20
        private const val TAG = "MemberManagerViewActivity"
        private const val DAUM_SEARCH_RESULT = 101
    }
}
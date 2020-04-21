package com.buel.holyhelpers.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.TelephonyManager
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.*
import com.buel.holyhelpers.management.FireStoreManager
import com.buel.holyhelpers.management.PointManager
import com.buel.holyhelpers.model.HolyModel
import com.buel.holyhelpers.model.UserModel
import com.buel.holyhelpers.view.DataReturnListener
import com.buel.holyhelpers.view.DataTypeListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.commonLib.Common
import com.commonLib.MaterialDailogUtil
import com.commonLib.PatternDefine
import com.commonLib.SuperToastUtil
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.logger.LoggerHelper
import java.io.FileNotFoundException
import java.io.IOException

class JoinActivity : BaseActivity(), View.OnClickListener {

    private var editText_name: EditText? = null
    private var editText_tell: EditText? = null
    private var editText_uid: EditText? = null
    private var imageView_profile: ImageView? = null
    private var mImageUri: Uri? = null
    private var adminUID: String? = null
    private var corpsName: String? = null
    private var tempHolyModelList: MutableList<HolyModel>? = null
    private var selectedUserType: UserType? = null
    private var isClickedSetSerial = false
    internal var nameStr: String = ""
    internal var tellStr: String = ""

    private val phone: String
        @SuppressLint("MissingPermission")
        get() {

            val wantPermission = Manifest.permission.READ_PHONE_STATE

            val phoneMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return if (ActivityCompat.checkSelfPermission(this@JoinActivity, wantPermission) != PackageManager.PERMISSION_GRANTED) {
                ""
            } else phoneMgr.line1Number
        }

    private var mBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        LoggerHelper.d("start JoinActivity")

        editText_uid = findViewById(R.id.joinActivity_edittext_corps_uid)
        editText_name = findViewById(R.id.joinActivity_edittext_name)
        editText_tell = findViewById(R.id.joinActivity_edittext_tell)
        imageView_profile = findViewById(R.id.joinActivity_imageivew_profile)
        imageView_profile!!.setOnClickListener(this)
        editText_uid!!.setOnClickListener(this)
        findViewById<View>(R.id.helper_btn).setOnClickListener(this)

        setFocusEditText(editText_name)
        setFocusEditText(editText_tell)

        setTopLayout(this)
        setViewMode()
    }

    /**
     * ViewMode 에 맞게 화면을 구성한다.
     */
    private fun setViewMode() {
        if (CommonData.adminMode == AdminMode.MODIFY) {  //수정 모드
            setModifyViewMode()
        } else {                                            //가입 모드
            joinStep1()
        }
    }

    private fun setJoinViewMode() {
        if (selectedUserType == UserType.SUB_ADMIN) {
            setTopDetailDesc(View.VISIBLE, "* 부 관리자는 운영관리와 제한적 권한을 가지고 있습니다.")
        } else {
            setTopDetailDesc(View.VISIBLE, "* 최고 관리자는 모든 권한을 가지고 있습니다. \n 각 교회마다 1계정만 존재합니다.")

            findViewById<View>(R.id.joinActivity_textInputLayout_corps).visibility = View.INVISIBLE
        }
        super.setTopTitleDesc("계정 등록")
    }

    private fun setModifyViewMode() {
        val holyModel = CommonData.holyModel
        val (userName, userPhotoUri, _, userTell, _, _, _, _, _, corpsName1) = CommonData.userModel

        editText_name!!.isClickable = false
        editText_name!!.isFocusableInTouchMode = false
        editText_name!!.setText(userName)
        editText_tell!!.setText(userTell)

        corpsName = corpsName1
        adminUID = CommonData.adminUid

        LoggerHelper.d("setModifyViewMode", CommonData.memberShipType)

        if (holyModel.uid != null) editText_uid!!.setText(holyModel.name)

        super.setTopTitleDesc("계정 수정")
        setTopDetailDesc(View.VISIBLE, "* 계정 수정 모드입니다.")

        try {
            LoggerHelper.d("userModel.userPhotoUri :  " + userPhotoUri!!)
            Glide.with(this@JoinActivity)
                    .load(userPhotoUri)
                    .apply(RequestOptions().circleCrop())
                    .into(imageView_profile!!)
        } catch (e: Exception) {
            LoggerHelper.e(e.message)
        }

    }

    private fun joinStep1() {
        super.setTopTitleDesc("계정 타입을 설정")

        MaterialDailogUtil.showSingleChoice(
                this@JoinActivity,
                "계정 타입을 설정해 주세요.",
                R.array.membership_option,
                object : MaterialDailogUtil.OnDialogSelectListner {
                    override fun onSelect(s: String) {
                        LoggerHelper.d("showSingleChoice  s : $s")
                        if (s == "0") { //super 관리자
                            selectedUserType = UserType.SUPER_ADMIN
                        } else if (s == "1") {// 부 관리자
                            selectedUserType = UserType.SUB_ADMIN
                        }

                        var userTypeName: String? = null
                        if (selectedUserType == UserType.SUPER_ADMIN)
                            userTypeName = "최고 관리자"
                        else if (selectedUserType == UserType.SUB_ADMIN)
                            userTypeName = "운영 관리자"

                        val okList = ArrayList<String>()
                        okList.add("동의합니다.")
                        okList.add("다시 선택하겠습니다.")

                        MaterialDailogUtil.showSingleChoice(
                                this@JoinActivity,
                                "[ $userTypeName ] 계정으로 선택하셨습니다. \n동의하십니까?",
                                okList,
                                object : MaterialDailogUtil.OnDialogSelectListner {
                                    override fun onSelect(s: String) {
                                        if (s == "0") { //super 관리자
                                            popToast("계정선택이 완료되었습니다.")
                                            setJoinViewMode()
                                        } else if (s == "1") {// 부 관리자
                                            joinStep1()
                                        }
                                    }
                                })

                    }
                })
    }

    @SuppressLint("ResourceType")
    override fun onClick(v: View) {
        when (v.id) {

            R.id.helper_btn -> TutorialViewerUtil.getCreateAccountTutorialModels(this@JoinActivity)
            //교회선택하기 클릭
            R.id.joinActivity_edittext_corps_uid -> onClickedSetSerial()

            //돌아가기 버튼 클릭
            R.id.top_bar_btn_back ->
                /*if (CommonData.getViewMode() == ViewMode.MODIFY ||
                        CommonData.getViewMode() == ViewMode.ADD_ACOUNT_SUB_ADMIN) {
                    goSettings();
                } else {
                    goLogin();
                } */
                goBackHistoryIntent()

            //저장버튼 클릭했을때
            R.id.top_bar_btn_ok ->

                //수정하기 모드일때 할때
                if (CommonData.adminMode == AdminMode.MODIFY) {
                    onOkclickedModify()
                } else {
                    onJoinClick(v)
                }

            //사진 등록하기 버튼 클릭
            R.id.joinActivity_imageivew_profile -> onProfileClick()
        }
    }

    private fun onClickedSetSerial() {
        /*if (CommonData.getViewMode() == ViewMode.MODIFY || CommonData.getViewMode() == ViewMode.ADD_ACOUNT_SUB_ADMIN) {
            SuperToastUtil.toastE(JoinActivity.this, "변경할수 없습니다.");
            return;
        }
*/
        if (selectedUserType == UserType.SUPER_ADMIN || CommonData.memberShipType == UserType.SUPER_ADMIN) {
            SuperToastUtil.toastE(this@JoinActivity, "SUPER_ADMIN 유저입니다.")
            return
        }

        LoggerHelper.d("onClickedSetSerial", "CommonData.getAdminMode() : " + CommonData.adminMode)
        LoggerHelper.d("onClickedSetSerial", "isClickedSetSerial : $isClickedSetSerial")
        if (CommonData.adminMode == AdminMode.MODIFY && !isClickedSetSerial && CommonData.memberShipType != UserType.SUPER_ADMIN) {
            MaterialDailogUtil.simpleYesNoDialog(this@JoinActivity, CommonString.CORP_NICK
                    + " 을/를 선택시 [운영 관리자] 권한을 다시 요청해야 합니다. 실행하시겠습니까?",
                    object : MaterialDailogUtil.OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            isClickedSetSerial = true
                            onClickedSetSerial()
                        }
                    })
            return
        }

        MaterialDailogUtil.simpleInputDoneDialog(this@JoinActivity, "교회 찾기!",
                "교회 이름만 넣어도 됩니다.", object : MaterialDailogUtil.OnDialogSelectListner {
            override fun onSelect(s: String) {
                var s = s

                s = s.replace(PatternDefine.PATTERN_BLANK.toRegex(), "")
                CommonData.strSearch = s

                FDDatabaseHelper.getAllCorpsStoreData(DataTypeListener.OnCompleteListener {
                    //LoggerHelper.d(CommonData.getHolyModelList())
                    var holyModels: ArrayList<HolyModel?> = arrayListOf()
                    it.documents.forEach { documentSnapshot ->
                        holyModels.add(documentSnapshot.toObject(HolyModel::class.java))
                    }

                    //val holyModels = CommonData.getHolyModelList()
                    val tempList = ArrayList<String>()
                    tempHolyModelList = ArrayList()
                    LoggerHelper.d("JoinActivity", "holyModels : " + holyModels.size)

                    var c: Int? = 0
                    for (elem in holyModels) {

                        LoggerHelper.d("JoinActivity", "CommonData.getStrSearch() : " + CommonData.strSearch)
                        LoggerHelper.d("JoinActivity", "elem.name : " + elem?.name)

                        if (elem?.name != null) {
                            val compareMemberName = elem.name.indexOf(CommonData.strSearch)
                            if (compareMemberName != -1) {
                                LoggerHelper.d("JoinActivity", "단체 추가합니다. => " + elem.name)
                                tempList.add(c!!, "교회이름: " + elem.name + "\n담임목사 : " + elem.owner +
                                        "\n교회주소 : " + elem.address + "\n교회전화 : " + elem.phone)
                                tempHolyModelList!!.add(elem)
                                c++
                            }
                        }
                    }

                    MaterialDailogUtil.showSingleChoice(this@JoinActivity, CommonString.CORP_NICK + " 을/를 선택하세요.",
                            tempList, object : MaterialDailogUtil.OnDialogSelectListner {
                        override fun onSelect(s: String) {

                            try {
                                LoggerHelper.d("s : $s")
                                LoggerHelper.d("s : " + tempHolyModelList!![Integer.parseInt(s)])
                                val holyModel = tempHolyModelList!![Integer.parseInt(s)]
                                LoggerHelper.d(holyModel.convertMap())

                                editText_uid!!.setText(holyModel.name)
                                adminUID = holyModel.adminUid
                                corpsName = holyModel.name

                            } catch (e: Exception) {
                                LoggerHelper.d(e.message)
                                LoggerHelper.d("선택을 하지 않은 경우")
                            }
                        }
                    })
                })
            }
        })
    }

    /**
     * 저장하기 버튼 클릭 : 수정하기 모드일 때
     */
    private fun onOkclickedModify() {
        val user = CommonData.firebaseUser!!
        val userModel = CommonData.userModel!!
        userModel.userName = editText_name!!.text.toString()
        userModel.userTell = editText_tell!!.text.toString()

        if (UserType.SUB_ADMIN == CommonData.memberShipType) {
            if (corpsName == null || adminUID == null) {
                sToast(CommonString.INFO_TITLE_SELECT_CORP, true)
                return
            }
            userModel.corpsName = corpsName
        }

        //isClickedSetSerial : true 이면 , 1. SUPER_ADMIN 이 아니고 2. 교회 선택하기를 클릭한 상태이다.
        if (isClickedSetSerial) {
            userModel.permission = "no"
        }
        userModel.userEmail = FirebaseAuth.getInstance().currentUser!!.email
        userModel.userType = CommonData.userModel.userType

        LoggerHelper.e(userModel.toString())
        if (mBitmap != null) {
            FDDatabaseHelper.setUploadImageSendUserStorage(userModel, mBitmap!!, DataReturnListener.OnCompleteListener { str ->
                userModel.userPhotoUri = str

                try {

                    FDDatabaseHelper.sendUserDataInsertUserModel(user.uid, userModel, DataTypeListener.OnCompleteListener { t: Boolean ->
                        if (UserType.SUPER_ADMIN != CommonData.memberShipType)
                            CommonData.adminUid = userModel.adminUID
                        goMain()
                    })
                } catch (e: Exception) {
                    SuperToastUtil.toast(this@JoinActivity, e.message)
                }
            })
        } else {
            try {

                FDDatabaseHelper.sendUserDataInsertUserModel(user.uid, userModel, DataTypeListener.OnCompleteListener { t: Boolean ->
                    if (UserType.SUPER_ADMIN != CommonData.memberShipType)
                        CommonData.adminUid = userModel.adminUID
                    goMain()
                })
            } catch (e: Exception) {
                SuperToastUtil.toast(this@JoinActivity, e.message)
            }

        }
    }

    /**
     * 저장하기 버튼을 클릭하였을 때
     *
     *
     * 1. 구조 확인 ( 공란, 이메일, 패스워드 ..)
     * 2. check macadress : 서버의 전체 유저의 mac adress 확인
     * 3. firebase create user
     *
     * @param v
     */
    private fun onJoinClick(v: View) {

        nameStr = editText_name!!.text.toString()
        tellStr = editText_tell!!.text.toString()

        //이미지가 없다면 기본 이미지를 쓴다.
        if (mImageUri == null) {
            mImageUri = Uri.parse("android.resource://" + packageName + "/" + R.drawable.man)
        }

        setUserDataOnFireBase()
    }

    private fun setUserDataOnFireBase() {

        val userModel = UserModel()
        if (nameStr.length <= 0 || tellStr.length <= 0) {
            sToast("공란이 있습니다.", true)
            return
        }

        try {
            userModel.userEmail = FirebaseAuth.getInstance().currentUser!!.email
        } catch (e: Exception) {
            popToast("설정하신 계정의 이메일이 없습니다. 관리자에게 문의하세요.")
            userModel.userEmail = ""
        }

        userModel.userName = nameStr
        userModel.userTell = tellStr
        userModel.macAddress = Common.getMacAdress()
        userModel.uid = FirebaseAuth.getInstance().currentUser!!.uid

        if (UserType.SUPER_ADMIN == selectedUserType) {
            userModel.permission = "ok"
            userModel.adminUID = FirebaseAuth.getInstance().uid
            CommonData.adminUid = userModel.adminUID
        } else if (UserType.SUB_ADMIN == selectedUserType) {
            if (corpsName == null || adminUID == null) {
                sToast(CommonString.INFO_TITLE_SELECT_CORP, true)
                return
            }

            userModel.corpsName = corpsName
            userModel.permission = "no"
            userModel.adminUID = adminUID
        }

        userModel.userType = selectedUserType!!.toString()

        LoggerHelper.d("mImageUri : " + mImageUri!!)
        LoggerHelper.d(userModel.toString())

        if (mBitmap == null) {
            val firebaseAuth = FirebaseAuth.getInstance()
            mImageUri = firebaseAuth.currentUser!!.photoUrl
            userModel.userPhotoUri = mImageUri!!.toString()
            popToast("프로필사진으로 대처합니다.")
            updateUserTable(userModel.uid, userModel)
            return
        }

        FDDatabaseHelper.setUploadImageSendUserStorage(userModel, mBitmap!!, DataReturnListener.OnCompleteListener { str ->
            userModel.userPhotoUri = str
            updateUserTable(userModel.uid, userModel)
        })
    }

    private fun updateUserTable(uid: String?, userModel: UserModel) {

        LoggerHelper.d("MACADRESS", userModel.macAddress)
        //progressDialog.show();
        try {
            FireStoreManager.sendUserDataInsertUserModel(userModel, DataTypeListener.OnCompleteListener {
                CommonData.memberShipType = selectedUserType!!
                CommonData.userModel = userModel
                if (selectedUserType == UserType.SUB_ADMIN) {
                    CommonData.adminUid = userModel.adminUID
                    PointManager.setPlusPoint(CommonData.personalJoinPoint)
                    Toast.makeText(this@JoinActivity, "회원가입이 완료되었습니다. '가입 포인트' " + CommonData.personalJoinPoint + " 지급됩니다.", Toast.LENGTH_SHORT).show()
                    goMain()
                } else if (selectedUserType == UserType.SUPER_ADMIN) {
                    CommonData.isTutoMode = true
                    goMain()
                }
            })
        } catch (e: Exception) {
            SuperToastUtil.toast(this@JoinActivity, e.message)
            LoggerHelper.e(e.message)
        }

    }

    private fun onProfileClick() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PIC_FROM_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PIC_FROM_ALBUM && requestCode == Activity.RESULT_OK);
        run {
            if (data == null) return
            mImageUri = data.data
            imageView_profile!!.setImageURI(mImageUri)
            val imgView = imageView_profile

            try {
                mBitmap = Common.getBitmapFromUri(this@JoinActivity, mImageUri)
                imgView!!.setImageBitmap(mBitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {

        private val PIC_FROM_ALBUM = 10
    }
}
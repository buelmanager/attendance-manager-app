package com.buel.holyhelper.view.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.buel.holyhelper.R
import com.buel.holyhelper.data.*
import com.buel.holyhelper.management.TeamManager
import com.buel.holyhelper.model.HolyModel
import com.buel.holyhelper.utils.SortMapUtil
import com.buel.holyhelper.view.DataTypeListener
import com.commonLib.Common
import com.commonLib.MaterialDailogUtil
import com.commonLib.SuperToastUtil
import com.orhanobut.logger.LoggerHelper
import java.util.*

class TeamManagerViewActivity : BaseActivity(), View.OnClickListener {
    internal lateinit var etName: EditText
    internal lateinit var etLeader: EditText
    internal lateinit var etEtc: EditText
    internal lateinit var tvDesc: TextView

    //var membersmap: MutableCollection<HolyModel.groupModel.teamModel> = CommonData.getTeamMap().values
    val data: ArrayList<HolyModel.groupModel.teamModel>
        get() {
            var teams = ArrayList<HolyModel.groupModel.teamModel>()

            try {
                var membersmap: MutableCollection<HolyModel.groupModel.teamModel> = CommonData.getTeamMap().values
                teams = membersmap
                        .filter { it.groupUid == CommonData.getGroupModel().uid }
                        as ArrayList<HolyModel.groupModel.teamModel>
                LoggerHelper.d(teams)

            } catch (e: Exception) {
            }
            return teams
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_manager_view)


        //tvDesc = findViewById(R.id.recycler_view_main_tv_desc)
        etName = findViewById(R.id.team_manager_activity_view_et_name)
        etLeader = findViewById(R.id.team_manager_activity_view_et_leader)
        etEtc = findViewById(R.id.team_manager_activity_view_et_etc)

        //etName.setText(CommonString.DEFINITION_NAME_U_NUMBER_NICK);
        //etEtc.setText(CommonString.DEFINITION_NAME_TEAM_NICK + "(필요한 경우 입력해주세요.)");
        //etLeader.setText(CommonString.DEFINITION_NAME_LEADER);

        setFocusEditText(etName)
        setFocusEditText(etLeader)
        setFocusEditText(etEtc)
        setTopLayout(this)

        if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            super.setTopTitleDesc("편집")

            etName.setText(getInteger(CommonData.getSelectedTeam().name).toString())
            etLeader.setText(CommonData.getSelectedTeam().leader)
            etEtc.setText(CommonData.getSelectedTeam().etc)

            val textViewGuide = findViewById<TextView>(R.id.manager_tv_title)
            textViewGuide.text = "* 아래의 내용을 수정할 수 있습니다."

        } else if (CommonData.getViewMode() == ViewMode.ADMIN) {
            super.setTopTitleDesc(CommonString.TEAM_NICK + " 생성")
        }
    }

    private fun setTitle(str: String) {
        tvDesc.text = str
    }

    private fun sendServer() {

        LoggerHelper.e("sendServer 1")
        LoggerHelper.e("CommonData.getHolyModel() : " + CommonData.getHolyModel())
        LoggerHelper.e("CommonData.getGroupModel() : " + CommonData.getGroupModel())

        isSaving = true
        if (CommonData.getHolyModel() == null || CommonData.getGroupModel() == null) {
            isSaving = false
            return
        }

        val teamArrayList = data

        if (etName.text.toString() == "") {
            SuperToastUtil.toast(this@TeamManagerViewActivity, CommonString.DEFINITION_NAME_U_NUMBER_NICK + " 을 입력해주세요.")
            isSaving = false
            return
        }
        var tempCnt = -1

        try {
            tempCnt = Integer.parseInt(etName.text.toString())
        } catch (e: Exception) {
            SuperToastUtil.toast(this@TeamManagerViewActivity, CommonString.DEFINITION_NAME_U_NUMBER_NICK + "에 '0' 이상의 숫자만 입력해주세요.")
            isSaving = false
            return
        }

        if (tempCnt < 0) {
            SuperToastUtil.toast(this@TeamManagerViewActivity, CommonString.DEFINITION_NAME_U_NUMBER_NICK + " 에 '0' 이상의 숫자만 입력해주세요.")
            isSaving = false
            return
        }

        val isCompare = getCompareData(etName.text.toString(), teamArrayList as ArrayList<HolyModel.groupModel.teamModel>)

        if (isCompare) {
            SuperToastUtil.toast(this@TeamManagerViewActivity, "같은" + CommonString.DEFINITION_NAME_U_NUMBER_NICK + "가 있습니다.")
            Log.d(TAG, "sendServer: isCompare = $isCompare")
            isSaving = false
            return
        }


        val teamManager = TeamManager()
        val teamModel = HolyModel.groupModel.teamModel()
        teamModel.name = etName.text.toString()
        teamModel.leader = etLeader.text.toString()
        teamModel.groupUid = CommonData.getGroupModel().uid

        if (etEtc.text.length > 0)
            teamModel.etc = etEtc.text.toString()
        else
            teamModel.etc = CommonData.getGroupModel().name

        if (CommonData.getAdminMode() == AdminMode.NORMAL) {

            teamManager.insert(teamModel) {
                Log.d(TAG, "onComplete: teamManager insert complete!!!")

                FDDatabaseHelper.getTeamDataToStore(DataTypeListener.OnCompleteListener {
                    if (CommonData.isTutoMode()) {
                        try {
                            val teams: ArrayList<HolyModel.groupModel.teamModel> = data //SortMapUtil.getSortTeamList() as ArrayList<HolyModel.groupModel.teamModel>
                            for (eleteam in teams) {
                                CommonData.setTeamModel(eleteam)
                            }
                        } catch (e: Exception) {
                        }

                        MaterialDailogUtil.simpleDoneDialog(this@TeamManagerViewActivity,
                                "#4 단계, 최초 회원 등록을 진행합니다.", object : MaterialDailogUtil.OnDialogSelectListner {
                            override fun onSelect(s: String) {
                                goSetAddMember()
                            }
                        })
                    } else {
                        isSaving = false
                        goTeamRecycler()
                    }
                })
            }
        } else if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            teamModel.uid = CommonData.getSelectedTeam().uid
            teamManager.modify(teamModel) {
                Log.d(TAG, "onComplete: teamManager modify complete!!!")

                FDDatabaseHelper.getTeamDataToStore(DataTypeListener.OnCompleteListener {
                    isSaving = false
                    goTeamRecycler()
                })
            }
        }
    }

    fun getCompareData(strComare: String, teams: ArrayList<HolyModel.groupModel.teamModel>): Boolean {
        if (CommonData.getAdminMode() == AdminMode.MODIFY) {
            if (CommonData.getSelectedTeam().name == strComare) {
                return false
            }
        }

        LoggerHelper.d("getCompareData start teams.size " + teams.size)
        for (eleTeam in teams) {
            LoggerHelper.d("eleTeam.name : " + eleTeam.name + " // strComare : " + strComare)
            if (SortMapUtil.getInteger(eleTeam.name) === SortMapUtil.getInteger(strComare) && eleTeam.groupUid == CommonData.getGroupModel().uid) {
                return true
            }
        }
        return false
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.top_bar_btn_ok -> {

                if (CommonData.getUserModel().userType != UserType.SUPER_ADMIN.toString()) {
                    val title = "권한이 없습니다."
                    val ment = CommonData.getUserModel().userType!! + " 유저는 해당 권한이없습니다. 관리자에게 문의하세요."
                    MaterialDailogUtil.simpleDoneDialog(this@TeamManagerViewActivity, title, ment, object : MaterialDailogUtil.OnDialogSelectListner {
                        override fun onSelect(s: String) {
                            goGroupRecycler()
                        }
                    })

                    isSaving = false
                    return
                }

                Common.hideKeyboard(this@TeamManagerViewActivity)

                if (!isSaving) {
                    sendServer()
                } else {
                    SuperToastUtil.toastE(this, "저장중입니다. 잠시만 기다리세요.")
                }
            }

            R.id.top_bar_btn_back -> goBackHistory()

            else -> {
            }
        }
    }

    companion object {

        private val TAG = "teamManagerViewActivity"

        /**
         * 문자열의 숫자를 뽑아 리턴
         *
         * @param str 숫자가 뒤에 들어가도록...
         * @return 리턴되는 숫자
         */
        fun getInteger(str: String): Int {
            var tempStr = ""
            //charAt를 이용하여 숫자가 아니면 넘기는 식으로 해서 뽑아 낼 수 있다.
            for (i in 0 until str.length) {
                // 48 ~ 57은 아스키 코드로 0~9이다.
                if (48 <= str[i].toInt() && str[i].toInt() <= 57)
                    tempStr += str[i]
            }
            return Integer.valueOf(tempStr)
        }
    }
}

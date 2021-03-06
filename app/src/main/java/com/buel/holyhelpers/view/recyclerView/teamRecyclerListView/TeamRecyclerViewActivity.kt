package com.buel.holyhelpers.view.recyclerView.teamRecyclerListView

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.*
import com.buel.holyhelpers.model.HolyModel
import com.buel.holyhelpers.utils.U.teamCovertList
import com.buel.holyhelpers.view.DataTypeListener
import com.buel.holyhelpers.view.SimpleListener
import com.buel.holyhelpers.view.activity.BaseActivity
import com.orhanobut.logger.LoggerHelper
import java.util.*

class TeamRecyclerViewActivity : BaseActivity(), View.OnClickListener {
    internal lateinit var recyclerView: RecyclerView
    internal var holderAdapter: TeamRecyclerViewAdapter? = null

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        setTitle("소그룹을 추가하세요.")

        //setLayout();
    }

    override fun onResume() {
        super.onResume()

        FDDatabaseHelper.getTeamDataToStore(DataTypeListener.OnCompleteListener { t ->
            //TODO develop branch에서 수정할것 : 리스트 없을때 fancy guide
            if (t.isEmpty) {
                //val ment = CommonString.GUIDE_FLOATING_BUTTON_ADD
                //val title = CommonString.INFO_TITLE_SELECT_TEAM
                //super.setGuideDailogAndOpenFabset(ment, title, super.fabFstActBtn)
            }
            setLayout()
        })
    }

    private fun setLayout() {
        val tvDesc = findViewById<TextView>(R.id.recycler_view_main_tv_desc)
        recyclerView = findViewById(R.id.recycler_view_main)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        //fab = findViewById(R.id.recycler_view_main_fab);
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0)
                    super@TeamRecyclerViewActivity.setAllFabVisibled(true)
                else if (dy > 0)
                    super@TeamRecyclerViewActivity.setAllFabVisibled(false)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        super.setTopLayout(this)
        super.setBaseFloatingActionButton()
        //super.setFabBackImg(super.fab2ndBtn, R.drawable.ic_anal);
        super.setFabSnd(ViewMode.BRIEFING)
        super.setFabFst(ViewMode.ATTENDANCE)
        //super.setFabBackImg(super.fabFstBtn, R.drawable.ic_atten_check);
        super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_note_add_black_24dp)

        var teams = ArrayList<HolyModel.groupModel.teamModel>()
        var group: HolyModel.groupModel? = null

        if (CommonData.getHolyModel() == null) {
            Toast.makeText(this, "교회/단체 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            goSelect()
            return
        }

        try {
            group = CommonData.getGroupModel()
            tvDesc.text = group!!.name

        } catch (e: Exception) {
            Toast.makeText(this, CommonString.INFO_TITLE_SELECTL_GROUP, Toast.LENGTH_SHORT).show()
            goSelect()
        }
        var membersmap: MutableCollection<HolyModel.groupModel.teamModel> = CommonData.getTeamMap().values

        try {
            teams = membersmap
                    .filter { it.groupUid == CommonData.getGroupModel().uid }
                    as ArrayList<HolyModel.groupModel.teamModel>
            LoggerHelper.d(teams)
        } catch (e: Exception) {
            super.setTopOkBtnVisibled(View.INVISIBLE)
            tvDesc.text = CommonString.TEAM_NICK + " 을/를 추가해주세요."
            //SuperToastUtil.toastE(this, "팀을 추가해주세요.");w
            val ment = CommonString.GUIDE_FLOATING_BUTTON_ADD
            val title = CommonString.INFO_TITLE_DONT_LIST_DATA
            super.setGuideDailogAndOpenFabset(ment, title, super.fabFstActBtn)
            return
        }

        holderAdapter = TeamRecyclerViewAdapter(teams, this)
        recyclerView.adapter = holderAdapter
        setAdminMode()
    }

    override fun setHelperButton() {
        val strHelper = ""
        TutorialViewerUtil.getTeamSelectTutorial(this@TeamRecyclerViewActivity)
    }

    override fun setActionButton() {
        CommonData.setAdminMode(AdminMode.NORMAL)
        goSetTeam()
        //CommonData.setHistoryClass(TeamRecyclerViewActivity::class.java as Class<*>)
    }

    override fun setFstFabBtn() {
        CommonData.setViewMode(ViewMode.ATTENDANCE)
        goMemberRecycler()
    }

    override fun setSndFabBtn() {
        CommonData.setViewMode(ViewMode.BRIEFING)
        goMain()
    }

    private fun setModifyMode() {
        //fab.hide();
        CommonData.setAdminMode(AdminMode.MODIFY)
        super.setTopOkBtnVisibled(View.VISIBLE)
        super.setTopOkBtnBackground(R.drawable.ic_clear_white_24dp)
        setViewAdminMode(AdminMode.MODIFY, "[ " + CommonData.getGroupModel().name + " ] " + CommonString.TEAM_NICK + " 수정")
    }

    private fun setAdminMode() {
        CommonData.setAdminMode(AdminMode.NORMAL)
        super.setTopOkBtnVisibled(View.VISIBLE)
        super.setTopOkBtnBackground(R.drawable.ic_m_settings_24dp)
        //fab.show();
        setViewAdminMode(AdminMode.NORMAL, CommonString.TEAM_NICK + "을 선택하세요.")
    }

    private fun setViewAdminMode(mode: AdminMode, title: String) {
        CommonData.setAdminMode(mode)
        setTitle(title)

        if (holderAdapter != null)
            holderAdapter!!.notifyDataSetChanged()
    }

    private fun setTitle(str: String) {
        super.setTopTitleDesc(str)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v.id == R.id.recycler_view_item_rl_main) {
            LoggerHelper.d("MemberShipRecyclerViewActivity", "v.getId()  : 2 " + v.id)
            FDDatabaseHelper.getMyCorps(SimpleListener.OnCompleteListener {
                CommonData.setMemberModel(null)
                goSelect()
            })

        } else if (v.id == R.id.recycler_view_item_btn_delete) {

            FDDatabaseHelper.getTeamDataToStore(DataTypeListener.OnCompleteListener {
                val reTeams: ArrayList<HolyModel.groupModel.teamModel>?

                var map: HashMap<String, HolyModel.groupModel.teamModel> = CommonData.getGroupModel().team as HashMap<String, HolyModel.groupModel.teamModel>
                reTeams = map.teamCovertList() as ArrayList<HolyModel.groupModel.teamModel>

                if (reTeams == null) {
                    Toast.makeText(this@TeamRecyclerViewActivity, CommonString.TEAM_NICK + " 리스트가 없습니다.", Toast.LENGTH_SHORT).show()
                    goSelect()
                }
                holderAdapter!!.setItemArrayList(reTeams)
                recyclerView.refreshDrawableState()
                if (holderAdapter != null) holderAdapter!!.notifyDataSetChanged()

                try {

                } catch (e: Exception) {
                    Toast.makeText(this@TeamRecyclerViewActivity, CommonString.TEAM_NICK + " 리스트가 없습니다.", Toast.LENGTH_SHORT).show()
                    goSelect()
                }

            })
        } else if (v.id == R.id.recycler_view_item_btn_select) {
            if (CommonData.getAdminMode() == AdminMode.NORMAL) {
                FDDatabaseHelper.getMyCorps(SimpleListener.OnCompleteListener {})
                //startActivity(new Intent(TeamRecyclerViewActivity.this, SelectViewActivity.class));
                //finish();
            } else if (CommonData.getAdminMode() == AdminMode.MODIFY) {
                goSetTeam()
                //CommonData.setHistoryClass(TeamRecyclerViewActivity::class.java)
            }
        } else if (v.id == R.id.top_bar_btn_back) {
            goSelect()
        } else if (v.id == R.id.top_bar_btn_ok) {
            if (CommonData.getAdminMode() == AdminMode.NORMAL)
                setModifyMode()
            else if (CommonData.getAdminMode() == AdminMode.MODIFY)
                setAdminMode()
        }
    }
}

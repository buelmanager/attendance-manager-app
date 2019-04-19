package com.buel.holyhelper.view.recyclerView.groupRecyclerListView

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buel.holyhelper.R
import com.buel.holyhelper.data.*
import com.buel.holyhelper.model.HolyModel
import com.buel.holyhelper.utils.SharedPreferenceUtil
import com.buel.holyhelper.utils.SortMapUtil
import com.buel.holyhelper.utils.U.groupCovertList
import com.buel.holyhelper.view.DataTypeListener
import com.buel.holyhelper.view.SimpleListener
import com.buel.holyhelper.view.activity.BaseActivity
import com.orhanobut.logger.LoggerHelper
import java.util.*

class RecyclerViewActivity : BaseActivity(), View.OnClickListener {
    internal lateinit var recyclerView: RecyclerView
    internal var holderAdapter: RecyclerView.Adapter<RecyclerViewHolder>? = null

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        recyclerView = findViewById(R.id.recycler_view_main)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        var groups = ArrayList<HolyModel.groupModel>()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0)
                    super@RecyclerViewActivity.setAllFabVisibled(true)
                else if (dy > 0)
                    super@RecyclerViewActivity.setAllFabVisibled(false)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                /*if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }*/
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        findViewById<View>(R.id.recycler_view_main_iv_close).setOnClickListener(this)

        //setFloatingActionButton(MemberShipRecyclerViewActivity.this);
        super.setBaseFloatingActionButton()
        //super.setFabBackImg(super.fab2ndBtn, R.drawable.ic_anal);
        super.setFabSnd(ViewMode.BRIEFING)
        super.setFabFst(ViewMode.ATTENDANCE)
        //super.setFabBackImg(super.fabFstBtn, R.drawable.ic_atten_check);
        super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_note_add)

        if (CommonData.getHolyModel() == null) {
            Toast.makeText(this, "교회/단체 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            goSelect()
            return
        }

        try {
            groups = SortMapUtil.getSortGroupList(CommonData.getHolyModel().group) as ArrayList<HolyModel.groupModel>
        } catch (e: Exception) {
            setTitle("부서를 추가해주세요.")
            super.setTopOkBtnVisibled(View.INVISIBLE)

            val ment = CommonString.GUIDE_FLOATING_BUTTON_ADD
            val title = CommonString.INFO_TITLE_DONT_LIST_DATA
            super.setGuideDailogAndOpenFabset(ment, title, super.fabFstActBtn)

            //SuperToastUtil.toastE(this, "그룹을 추가해주세요.");
            return
        }

        holderAdapter = RecyclerViewAdapter(groups, this)
        recyclerView.adapter = holderAdapter
        setAdminMode()
        super.setTopLayout(this)
    }

    override fun setHelperButton() {

        TutorialViewerUtil.getGroupSelectTutorial(this@RecyclerViewActivity)

        /*if (CommonData.getViewMode() == ViewMode.ADMIN) {
            strHelper = "<strong>† 수정/관리</strong><br> " +
                    "버튼을 클릭하여 그룹 선택하세요." + "<br><br>" +
                    "상단 설정버튼을 클릭하면 <br>그룹 수정/삭제 관리가 가능합니다." + "<br><br>" +

                    "<strong>† 추가 기능</strong><br> " +
                    "하단의 그룹 추가 버튼을 클릭하면 <br>그룹추가가 가능합니다.<br><br>";
        }


        MaterialDailogUtil.noticeDialog(
                FoldCellRecyclerViewActivity.this,
                strHelper,
                CommonString.INFO_HELPER_TITLE,
                new MaterialDailogUtil.OnDialogSelectListner() {
                    @Override
                    public void onSelect(String s) {
                        CommonData.setIsFstEnter(false);
                        LoggerHelper.d("CommonData.getIsFstEnter() : " + CommonData.getIsFstEnter());
                    }
                });*/
    }

    override fun setActionButton() {
        CommonData.setAdminMode(AdminMode.NORMAL)
        goSetGroup()
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

        super.setTopOkBtnVisibled(View.VISIBLE)
        super.setTopOkBtnBackground(R.drawable.ic_clear_white_24dp)
        CommonData.setAdminMode(AdminMode.MODIFY)
        setViewAdminMode(AdminMode.MODIFY, "[ " + CommonData.getHolyModel().name + " ] " + CommonString.GROUP_NICK + "  수정")
    }

    private fun setAdminMode() {
        CommonData.setAdminMode(AdminMode.NORMAL)
        super.setTopOkBtnVisibled(View.VISIBLE)
        super.setTopOkBtnBackground(R.drawable.ic_m_settings_24dp)
        setViewAdminMode(AdminMode.NORMAL, CommonString.INFO_TITLE_SELECTL_GROUP)
    }

    private fun setViewAdminMode(mode: AdminMode, title: String) {
        CommonData.setAdminMode(mode)
        setTitle(title)

        if (holderAdapter != null)
            holderAdapter!!.notifyDataSetChanged()
    }

    private fun setTitle(str: String) {
        //tvDesc.setText(str);
        super.setTopTitleDesc(str)
    }

    /**
     * floating
     *
     * @param context
     */
    private fun setFloatingActionButton(context: Context) {

    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v.id == R.id.recycler_view_item_rl_main) {
            LoggerHelper.d("MemberShipRecyclerViewActivity", "v.getId()  : 2 " + v.id)
            FDDatabaseHelper.getMyCorps(SimpleListener.OnCompleteListener {
                CommonData.setTeamModel(null)
                CommonData.setMemberModel(null)
                SharedPreferenceUtil.initTeamModel()

                goTeamRecycler()
            })
        } else if (v.id == R.id.recycler_view_item_btn_delete) {
            LoggerHelper.d("MemberShipRecyclerViewActivity", "v.getId()  : 1 " + v.id)
            FDDatabaseHelper.getMyCorps(SimpleListener.OnCompleteListener {
                FDDatabaseHelper.getGroupDataToStore(DataTypeListener.OnCompleteListener {
                    val groupMap = CommonData.getHolyModel().group as HashMap<String, HolyModel.groupModel>
                    try {
                        val groupList = groupMap.groupCovertList()              //SortMapUtil.getSortGroupList(groupMap)
                        (holderAdapter as RecyclerViewAdapter).setItemArrayList(groupList as List<HolyModel.groupModel>)
                        recyclerView.refreshDrawableState()
                    } catch (e: Exception) {
                        Toast.makeText(this@RecyclerViewActivity, "그룹리스트가 없습니다.", Toast.LENGTH_SHORT).show()
                        goSelect()
                    }

                    holderAdapter!!.notifyDataSetChanged()
                })
            })
        } else if (v.id == R.id.recycler_view_item_btn_select) {
            if (CommonData.getAdminMode() == AdminMode.NORMAL) {
            } else if (CommonData.getAdminMode() == AdminMode.MODIFY) {
                goSetGroup()
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

    companion object {
        private val TAG = "MemberRecyclerViewActivity"
    }
}

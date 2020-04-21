package com.buel.holyhelpers.view.recyclerView.foldcellRecyclerView

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buel.holyhelpers.R
import com.buel.holyhelpers.data.*
import com.buel.holyhelpers.model.HolyModel
import com.buel.holyhelpers.utils.U.memberaCovertList
import com.buel.holyhelpers.view.SimpleListener
import com.buel.holyhelpers.view.activity.BaseActivity
import com.orhanobut.logger.LoggerHelper
import kotlinx.android.synthetic.main.activity_recycler_view.*
import java.util.*

class FoldCellRecyclerViewActivity : BaseActivity(), View.OnClickListener {
    internal lateinit var recyclerView: RecyclerView
    lateinit var holderAdapter: FoldCellRecyclerViewAdapter

    private var membersArrayList: ArrayList<HolyModel.memberModel>? = null

    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fd_cell_recycler_view)

        recyclerView = recycler_view_main
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        recycler_view_main_iv_close.setOnClickListener(this)

        if (CommonData.holyModel == null) {
            Toast.makeText(this, "교회/단체 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            goSelect()
            return
        }

        try {
            membersArrayList = getData()
        } catch (e: Exception) {
            setTitle("부서를 추가해주세요.")
            super.setTopOkBtnVisibled(View.INVISIBLE)
            val ment = CommonString.GUIDE_FLOATING_BUTTON_ADD
            val title = CommonString.INFO_TITLE_DONT_LIST_DATA
            super.setGuideDailogAndOpenFabset(ment, title, super.fabFstActBtn)
            return
        }

        holderAdapter = FoldCellRecyclerViewAdapter(membersArrayList, this)
        recyclerView.adapter = holderAdapter

        setAdminMode()
        setFabButtons()

        super.setTopLayout(this)
    }

    fun getData(): ArrayList<HolyModel.memberModel> {
        val tempMap = CommonData.membersMap as HashMap<String, HolyModel.memberModel>
        return tempMap.memberaCovertList() as ArrayList<HolyModel.memberModel>
    }

    fun refresh(){
        FDDatabaseHelper.getAllcorpsMembers( SimpleListener.OnCompleteListener {
            membersArrayList = getData()

            holderAdapter.setItemArrayList(membersArrayList)
            recyclerView.refreshDrawableState()
            holderAdapter?.notifyDataSetChanged()
            LoggerHelper.d("FoldCellRecyclerViewActivity > onResume")
        })
    }
    override fun onResume() {
        refresh()
        super.onResume()
    }

    private fun setFabButtons() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0)
                    super@FoldCellRecyclerViewActivity.setAllFabVisibled(true)
                else if (dy > 0)
                    super@FoldCellRecyclerViewActivity.setAllFabVisibled(false)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                /*if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }*/
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        super.setBaseFloatingActionButton()
        super.setFabSnd(ViewMode.BRIEFING)
        super.setFabFst(ViewMode.ATTENDANCE)
        super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_note_add)
    }

    override fun setHelperButton() {
        TutorialViewerUtil.getGroupSelectTutorial(this@FoldCellRecyclerViewActivity)
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

    private fun setAdminMode() {
        CommonData.setAdminMode(AdminMode.NORMAL)
        //super.setTopOkBtnVisibled(View.VISIBLE)
        //super.setTopOkBtnBackground(R.drawable.ic_m_settings_24dp)
        setTopTitleDesc(" 교적 관리 ")
        if (holderAdapter != null) holderAdapter!!.notifyDataSetChanged()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        refresh()
        when (v.id) {
            R.id.top_bar_btn_back -> goSelect()
            R.id.top_bar_btn_ok -> ""
        }
    }

    companion object {
        private val TAG = "MemberRecyclerViewActivity"
    }
}

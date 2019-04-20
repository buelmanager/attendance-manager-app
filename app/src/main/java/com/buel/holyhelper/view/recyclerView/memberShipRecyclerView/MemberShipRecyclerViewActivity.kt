package com.buel.holyhelper.view.recyclerView.memberShipRecyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.buel.holyhelper.R
import com.buel.holyhelper.data.AdminMode
import com.buel.holyhelper.data.CommonData
import com.buel.holyhelper.data.CommonString
import com.buel.holyhelper.data.FDDatabaseHelper
import com.buel.holyhelper.data.UserType
import com.buel.holyhelper.data.ViewMode
import com.buel.holyhelper.model.UserModel
import com.buel.holyhelper.view.SimpleListener
import com.buel.holyhelper.view.activity.BaseActivity
import com.commonLib.MaterialDailogUtil
import com.orhanobut.logger.LoggerHelper

import java.util.ArrayList

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MemberShipRecyclerViewActivity : BaseActivity(), View.OnClickListener {
    internal var recyclerView: RecyclerView
    internal var holderAdapter: RecyclerView.Adapter<MemberShipRecyclerViewHolder>? = null
    internal var userModels = ArrayList<UserModel>()
    internal var userType: String? = null
    @SuppressLint("LongLogTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        super.setTopOkBtnVisibled(View.INVISIBLE)
        val intent = intent
        userType = intent.extras!!.getString("type")

        FDDatabaseHelper.getSubAdminList(UserType.SUB_ADMIN.toString(), { userModelList ->
            userModels = userModelList
            setRecyclerVeiw()
        })
    }

    private fun setRecyclerVeiw() {
        recyclerView = findViewById(R.id.recycler_view_main)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0)
                    super@MemberShipRecyclerViewActivity.setAllFabVisibled(true)
                else if (dy > 0)
                    super@MemberShipRecyclerViewActivity.setAllFabVisibled(false)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                /*if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }*/
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        findViewById<View>(R.id.recycler_view_main_iv_close).setOnClickListener(this)
        super.setTopLayout(this)
        super.setBaseFloatingActionButton()
        super.setFabSnd(ViewMode.BRIEFING)
        super.setFabFst(ViewMode.ATTENDANCE)
        //super.setFabBackImg(super.fabFstBtn, R.drawable.ic_atten_check);
        super.setFabBackImg(super.fabFstActBtn, R.drawable.ic_add_member)

        try {
            userModels.size
        } catch (e: Exception) {
            Toast.makeText(this, "USER 목록이 없습니다.", Toast.LENGTH_SHORT).show()
            goSettings()
            return
        }

        holderAdapter = MemberShipRecyclerViewAdapter(this@MemberShipRecyclerViewActivity, userModels, this)
        recyclerView.adapter = holderAdapter
        setAdminMode()
    }

    override fun setHelperButton() {

        var strHelper = ""

        if (CommonData.getViewMode() == ViewMode.ADMIN) {
            strHelper = "<strong>† 수정/관리</strong><br> " +
                    "버튼을 클릭하여 그룹 선택하세요." + "<br><br>" +
                    "상단 설정버튼을 클릭하면 <br>그룹 수정/삭제 관리가 가능합니다." + "<br><br>" +

                    "<strong>† 추가 기능</strong><br> " +
                    "하단의 그룹 추가 버튼을 클릭하면 <br>그룹추가가 가능합니다.<br><br>"
        }


        MaterialDailogUtil.noticeDialog(
                this@MemberShipRecyclerViewActivity,
                strHelper,
                CommonString.INFO_HELPER_TITLE,
                { s ->
                    CommonData.setIsFstEnter(false)
                    LoggerHelper.d("CommonData.getIsFstEnter() : " + CommonData.getIsFstEnter())
                })
    }

    override fun setActionButton() {
        CommonData.setViewMode(ViewMode.ADMIN)
        goSetGroup()
        CommonData.setHistoryClass(MemberShipRecyclerViewActivity::class.java as Class<*>)
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
        //super.setTopOkBtnVisibled(View.VISIBLE);
        super.setTopOkBtnBackground(R.drawable.ic_clear_white_24dp)
        //setViewMode(ViewMode.MODIFY, "[ " + CommonData.getHolyModel().name + " ] 멤버쉽 리스트 수정");
        setTitle("[ " + CommonData.getHolyModel().name + " ] 멤버쉽 리스트 수정")
        CommonData.setAdminMode(AdminMode.MODIFY)
    }

    private fun setAdminMode() {
        //super.setTopOkBtnVisibled(View.VISIBLE);
        super.setTopOkBtnBackground(R.drawable.ic_m_settings_24dp)
        setViewMode(ViewMode.ADMIN, "[ " + userType + " ] 계정수는 총 [ " + userModels.size + " ] 개")
    }

    private fun setViewMode(mode: ViewMode, title: String) {
        CommonData.setViewMode(mode)
        setTitle(title)

        if (holderAdapter != null)
            holderAdapter!!.notifyDataSetChanged()
    }

    private fun setTitle(str: String) {
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
            FDDatabaseHelper.getMyCorps(SimpleListener.OnCompleteListener { })
        } else if (v.id == R.id.recycler_view_item_btn_delete) {

            Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
            return
            /*LoggerHelper.d("MemberShipRecyclerViewActivity", "v.getId()  : 1 " + v.getId());
            FDDatabaseHelper.getMyCorps(new FDDatabaseHelper.onFDDCallbackListener() {
                @Override
                public void onFromDataComplete(int DataCode, DataSnapshot dataSnapshot) {
                    holderAdapter.notifyDataSetChanged();
                    try {
                        ((MemberShipRecyclerViewAdapter) holderAdapter).setItemArrayList(userModels);
                        recyclerView.refreshDrawableState();
                    } catch (Exception e) {
                        Toast.makeText(MemberShipRecyclerViewActivity.this, "그룹리스트가 없습니다.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MemberShipRecyclerViewActivity.this, SelectViewActivity.class));
                        finish();
                    }
                }
            });*/
        } else if (v.id == R.id.recycler_view_item_btn_select) {

            Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
            return
            /*
            if (CommonData.getViewMode() == ViewMode.ADMIN) {

            } else if (CommonData.getViewMode() == ViewMode.MODIFY) {

            }*/
        } else if (v.id == R.id.top_bar_btn_back) {
            goSettings()
        } else if (v.id == R.id.top_bar_btn_ok) {

            Toast.makeText(this, "권한이 없습니다.", Toast.LENGTH_SHORT).show()
            return
            /*
            if (CommonData.getViewMode() == ViewMode.ADMIN)
                setModifyMode();
            else if (CommonData.getViewMode() == ViewMode.MODIFY)
                setAdminMode();*/
        }
    }

    companion object {

        private val TAG = "MemberRecyclerViewActivity"
    }
}
